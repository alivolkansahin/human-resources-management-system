package org.musketeers.service;

import org.musketeers.dto.request.DayOffCancelRequestDto;
import org.musketeers.dto.request.DayOffCreateRequestDto;
import org.musketeers.dto.request.DayOffUpdateRequestDto;
import org.musketeers.dto.response.DayOffGetAllMyRequestsResponseDto;
import org.musketeers.dto.response.DayOffGetAllRequestsResponseDto;
import org.musketeers.entity.DayOff;
import org.musketeers.entity.enums.ERequestStatus;
import org.musketeers.exception.DayOffServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.model.GetPersonnelDetailsForDayOffRequestModel;
import org.musketeers.rabbitmq.model.GetPersonnelIdAndCompanyIdForDayOffRequestModel;
import org.musketeers.rabbitmq.model.SendDayOffStatusChangeNotificationModel;
import org.musketeers.rabbitmq.producer.GetPersonnelDetailsForDayOffRequestProducer;
import org.musketeers.rabbitmq.producer.GetPersonnelIdAndCompanyIdForDayOffRequestProducer;
import org.musketeers.rabbitmq.producer.SendDayOffStatusChangeNotificationProducer;
import org.musketeers.repository.DayOffRepository;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DayOffService extends ServiceManager<DayOff, String> {

    private final DayOffRepository dayOffRepository;

    private final JwtTokenManager jwtTokenManager;

    private final GetPersonnelIdAndCompanyIdForDayOffRequestProducer getPersonnelIdAndCompanyIdForDayOffRequestProducer;

    private final SendDayOffStatusChangeNotificationProducer sendDayOffStatusChangeNotificationProducer;

    private final GetPersonnelDetailsForDayOffRequestProducer getPersonnelDetailsForDayOffRequestProducer;

    private static final String SUPERVISOR_ROLE = "SUPERVISOR";
    private static final String PERSONNEL_ROLE = "PERSONNEL";

    public DayOffService(DayOffRepository dayOffRepository, JwtTokenManager jwtTokenManager, GetPersonnelIdAndCompanyIdForDayOffRequestProducer getPersonnelIdAndCompanyIdForDayOffRequestProducer, SendDayOffStatusChangeNotificationProducer sendDayOffStatusChangeNotificationProducer, GetPersonnelDetailsForDayOffRequestProducer getPersonnelDetailsForDayOffRequestProducer) {
        super(dayOffRepository);
        this.dayOffRepository = dayOffRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.getPersonnelIdAndCompanyIdForDayOffRequestProducer = getPersonnelIdAndCompanyIdForDayOffRequestProducer;
        this.sendDayOffStatusChangeNotificationProducer = sendDayOffStatusChangeNotificationProducer;
        this.getPersonnelDetailsForDayOffRequestProducer = getPersonnelDetailsForDayOffRequestProducer;
    }

    private String validateUserRoleAndRetrieveAuthId(String token, String roleString) {
        List<String> claimsFromToken = jwtTokenManager.getClaimsFromToken(token);
        String role = claimsFromToken.get(1);
        if(!role.equals(roleString)) throw new DayOffServiceException(ErrorType.INVALID_ROLE);
        return claimsFromToken.get(0);
    }


    public String createRequest(DayOffCreateRequestDto dto) {
        String authId = validateUserRoleAndRetrieveAuthId(dto.getToken(), PERSONNEL_ROLE);
        GetPersonnelIdAndCompanyIdForDayOffRequestModel responseModel = getPersonnelIdAndCompanyIdForDayOffRequestProducer.getPersonnelIdAndCompanyIdFromPersonnelService(authId);
        String personnelId = responseModel.getPersonnelId();
        String companyId = responseModel.getCompanyId();
        List<DayOff> personnelPendingRequests = dayOffRepository.findAllByPersonnelIdAndRequestStatus(personnelId, ERequestStatus.PENDING);
        if(!personnelPendingRequests.isEmpty()) throw new DayOffServiceException(ErrorType.PENDING_REQUEST_EXISTS);
        save(DayOff.builder()
                .personnelId(personnelId)
                .companyId(companyId)
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build());
        return "Request Created!";
    }

    public String cancelRequest(DayOffCancelRequestDto dto) {
        String authId = validateUserRoleAndRetrieveAuthId(dto.getToken(), PERSONNEL_ROLE);
        GetPersonnelIdAndCompanyIdForDayOffRequestModel responseModel = getPersonnelIdAndCompanyIdForDayOffRequestProducer.getPersonnelIdAndCompanyIdFromPersonnelService(authId);
        String personnelId = responseModel.getPersonnelId();
        DayOff dayOff = findById(dto.getRequestId());
        if(!dayOff.getPersonnelId().equals(personnelId)) throw new DayOffServiceException(ErrorType.INVALID_PERSON);
        if(!dayOff.getRequestStatus().equals(ERequestStatus.PENDING)) throw new DayOffServiceException(ErrorType.INVALID_PARAMETER);
        dayOff.setRequestStatus(ERequestStatus.CANCELLED);
        update(dayOff);
        return "Request Cancelled!";
    }

    public String updateRequestStatus(DayOffUpdateRequestDto dto) {
        String authId = validateUserRoleAndRetrieveAuthId(dto.getToken(), SUPERVISOR_ROLE);
        GetPersonnelIdAndCompanyIdForDayOffRequestModel responseModel = getPersonnelIdAndCompanyIdForDayOffRequestProducer.getPersonnelIdAndCompanyIdFromPersonnelService(authId);
        String companyId = responseModel.getCompanyId();
        DayOff dayOff = findById(dto.getRequestId());
        if(!dayOff.getCompanyId().equals(companyId)) throw new DayOffServiceException(ErrorType.INVALID_SUPERVISOR);
        if(!dayOff.getRequestStatus().equals(ERequestStatus.PENDING)) throw new DayOffServiceException(ErrorType.INVALID_PARAMETER);
        dayOff.setRequestStatus(ERequestStatus.valueOf(dto.getDecision()));
        update(dayOff);
        sendDayOffStatusChangeNotificationToPersonnelService(dayOff);
        return "Operation completed successfully";
    }

    private void sendDayOffStatusChangeNotificationToPersonnelService(DayOff dayOff) {
        SendDayOffStatusChangeNotificationModel requestModel = SendDayOffStatusChangeNotificationModel.builder()
                .personnelId(dayOff.getPersonnelId())
                .requestDescription(dayOff.getDescription())
                .requestStartDate(dayOff.getStartDate())
                .requestEndDate(dayOff.getEndDate())
                .updatedStatus(dayOff.getRequestStatus().toString())
                .requestCreatedAt(dayOff.getCreatedAt())
                .requestUpdatedAt(dayOff.getUpdatedAt())
                .build();
        sendDayOffStatusChangeNotificationProducer.sendNotificationToPersonnelService(requestModel);
    }

    public List<DayOffGetAllRequestsResponseDto> getAllRequestsForSupervisor(String token) {
        String authId = validateUserRoleAndRetrieveAuthId(token, SUPERVISOR_ROLE);
        GetPersonnelIdAndCompanyIdForDayOffRequestModel responseModel = getPersonnelIdAndCompanyIdForDayOffRequestProducer.getPersonnelIdAndCompanyIdFromPersonnelService(authId);
        String companyId = responseModel.getCompanyId();
        List<DayOff> dayOffRequests = dayOffRepository.findAllByCompanyId(companyId);
        if(dayOffRequests.isEmpty()) return Collections.emptyList();
        List<String> personnelIds = dayOffRequests.stream().map(DayOff::getPersonnelId).toList();
        List<GetPersonnelDetailsForDayOffRequestModel> responseModelList = getPersonnelDetailsForDayOffRequestProducer.getPersonnelDetailsFromPersonnelService(personnelIds);
        return prepareGetAllRequestsResponseDto(dayOffRequests, responseModelList);
    }

    private List<DayOffGetAllRequestsResponseDto> prepareGetAllRequestsResponseDto(List<DayOff> dayOffRequests, List<GetPersonnelDetailsForDayOffRequestModel> responseModelList) {
        List<DayOffGetAllRequestsResponseDto> responseDtoList = new ArrayList<>();
        for (int i = 0; i < dayOffRequests.size(); i++) {
            responseDtoList.add(DayOffGetAllRequestsResponseDto.builder()
                    .id(dayOffRequests.get(i).getId())
                    .personnelId(responseModelList.get(i).getPersonnelId())
                    .name(responseModelList.get(i).getName())
                    .lastName(responseModelList.get(i).getLastName())
                    .image(responseModelList.get(i).getImage())
                    .email(responseModelList.get(i).getEmail())
                    .dayOff(responseModelList.get(i).getDayOff())
                    .description(dayOffRequests.get(i).getDescription())
                    .startDate(dayOffRequests.get(i).getStartDate())
                    .endDate(dayOffRequests.get(i).getEndDate())
                    .requestStatus(dayOffRequests.get(i).getRequestStatus().toString())
                    .createdAt(Instant.ofEpochMilli(dayOffRequests.get(i).getCreatedAt())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate())
                    .updatedAt(Instant.ofEpochMilli(dayOffRequests.get(i).getUpdatedAt())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate())
                    .build());
        }
        return responseDtoList;
    }

    public List<DayOffGetAllMyRequestsResponseDto> getAllMyRequestsForPersonnel(String token) {
        String authId = validateUserRoleAndRetrieveAuthId(token, PERSONNEL_ROLE);
        GetPersonnelIdAndCompanyIdForDayOffRequestModel responseModel = getPersonnelIdAndCompanyIdForDayOffRequestProducer.getPersonnelIdAndCompanyIdFromPersonnelService(authId);
        String personnelId = responseModel.getPersonnelId();
        List<DayOff> personnelDayOffRequests = dayOffRepository.findAllByPersonnelId(personnelId);
        return prepareGetAllMyRequestsResponseDto(personnelDayOffRequests);
    }

    private List<DayOffGetAllMyRequestsResponseDto> prepareGetAllMyRequestsResponseDto(List<DayOff> personnelDayOffRequests) {
        List<DayOffGetAllMyRequestsResponseDto> responseDtoList = new ArrayList<>();
        for (DayOff eachDayOffRequest : personnelDayOffRequests) {
            responseDtoList.add(DayOffGetAllMyRequestsResponseDto.builder()
                    .id(eachDayOffRequest.getId())
                    .description(eachDayOffRequest.getDescription())
                    .startDate(eachDayOffRequest.getStartDate())
                    .endDate(eachDayOffRequest.getEndDate())
                    .requestStatus(eachDayOffRequest.getRequestStatus().toString())
                    .createdAt(Instant.ofEpochMilli(eachDayOffRequest.getCreatedAt())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate())
                    .updatedAt(Instant.ofEpochMilli(eachDayOffRequest.getUpdatedAt())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate())
                    .build());
        }
        return responseDtoList;
    }
}
