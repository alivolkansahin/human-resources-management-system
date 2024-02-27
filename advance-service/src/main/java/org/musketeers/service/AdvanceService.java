package org.musketeers.service;

import org.musketeers.dto.request.AdvanceCancelRequestDto;
import org.musketeers.dto.request.AdvanceCreateRequestDto;
import org.musketeers.dto.request.AdvanceUpdateRequestDto;
import org.musketeers.dto.response.AdvanceGetAllMyRequestsResponseDto;
import org.musketeers.dto.response.AdvanceGetAllRequestsResponseDto;
import org.musketeers.entity.Advance;
import org.musketeers.entity.enums.ERequestStatus;
import org.musketeers.entity.enums.EReason;
import org.musketeers.exception.AdvanceServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.model.GetPersonnelDetailsForAdvanceRequestModel;
import org.musketeers.rabbitmq.model.GetPersonnelIdAndCompanyIdForAdvanceRequestModel;
import org.musketeers.rabbitmq.model.SendAdvanceStatusChangeNotificationModel;
import org.musketeers.rabbitmq.producer.GetPersonnelDetailsForAdvanceRequestProducer;
import org.musketeers.rabbitmq.producer.GetPersonnelIdAndCompanyIdForAdvanceRequestProducer;
import org.musketeers.rabbitmq.producer.SendAdvanceStatusChangeNotificationProducer;
import org.musketeers.repository.AdvanceRepository;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AdvanceService extends ServiceManager<Advance, String> {

    private final AdvanceRepository advanceRepository;

    private final JwtTokenManager jwtTokenManager;

    private final GetPersonnelIdAndCompanyIdForAdvanceRequestProducer getPersonnelIdAndCompanyIdForAdvanceRequestProducer;

    private final SendAdvanceStatusChangeNotificationProducer sendAdvanceStatusChangeNotificationProducer;

    private final GetPersonnelDetailsForAdvanceRequestProducer getPersonnelDetailsForAdvanceRequestProducer;

    private static final String SUPERVISOR_ROLE = "SUPERVISOR";

    private static final String PERSONNEL_ROLE = "PERSONNEL";

    public AdvanceService(AdvanceRepository advanceRepository, JwtTokenManager jwtTokenManager, GetPersonnelIdAndCompanyIdForAdvanceRequestProducer getPersonnelIdAndCompanyIdForAdvanceRequestProducer, SendAdvanceStatusChangeNotificationProducer sendAdvanceStatusChangeNotificationProducer, GetPersonnelDetailsForAdvanceRequestProducer getPersonnelDetailsForAdvanceRequestProducer) {
        super(advanceRepository);
        this.advanceRepository = advanceRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.getPersonnelIdAndCompanyIdForAdvanceRequestProducer = getPersonnelIdAndCompanyIdForAdvanceRequestProducer;
        this.sendAdvanceStatusChangeNotificationProducer = sendAdvanceStatusChangeNotificationProducer;
        this.getPersonnelDetailsForAdvanceRequestProducer = getPersonnelDetailsForAdvanceRequestProducer;
    }

    private String validateUserRoleAndRetrieveAuthId(String token, String roleString) {
        List<String> claimsFromToken = jwtTokenManager.getClaimsFromToken(token);
        String role = claimsFromToken.get(1);
        if(!role.equals(roleString)) throw new AdvanceServiceException(ErrorType.INVALID_ROLE);
        return claimsFromToken.get(0);
    }


    public String createRequest(AdvanceCreateRequestDto dto) {
        String authId = validateUserRoleAndRetrieveAuthId(dto.getToken(), PERSONNEL_ROLE);
        GetPersonnelIdAndCompanyIdForAdvanceRequestModel responseModel = getPersonnelIdAndCompanyIdForAdvanceRequestProducer.getPersonnelIdAndCompanyIdFromPersonnelService(authId);
        String personnelId = responseModel.getPersonnelId();
        String companyId = responseModel.getCompanyId();
        List<Advance> personnelPendingRequests = advanceRepository.findAllByPersonnelIdAndRequestStatus(personnelId, ERequestStatus.PENDING);
        if(!personnelPendingRequests.isEmpty()) throw new AdvanceServiceException(ErrorType.PENDING_REQUEST_EXISTS);
        save(Advance.builder()
                .personnelId(personnelId)
                .companyId(companyId)
                .reason(EReason.valueOf(dto.getReason()))
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .build());
        return "Request Created!";
    }

    public String cancelRequest(AdvanceCancelRequestDto dto) {
        String authId = validateUserRoleAndRetrieveAuthId(dto.getToken(), PERSONNEL_ROLE);
        GetPersonnelIdAndCompanyIdForAdvanceRequestModel responseModel = getPersonnelIdAndCompanyIdForAdvanceRequestProducer.getPersonnelIdAndCompanyIdFromPersonnelService(authId);
        String personnelId = responseModel.getPersonnelId();
        Advance advance = findById(dto.getRequestId());
        if(!advance.getPersonnelId().equals(personnelId)) throw new AdvanceServiceException(ErrorType.INVALID_PERSON);
        if(!advance.getRequestStatus().equals(ERequestStatus.PENDING)) throw new AdvanceServiceException(ErrorType.INVALID_PARAMETER);
        advance.setRequestStatus(ERequestStatus.CANCELLED);
        update(advance);
        return "Request Cancelled!";
    }

    public String updateRequestStatus(AdvanceUpdateRequestDto dto) {
        String authId = validateUserRoleAndRetrieveAuthId(dto.getToken(), SUPERVISOR_ROLE);
        GetPersonnelIdAndCompanyIdForAdvanceRequestModel responseModel = getPersonnelIdAndCompanyIdForAdvanceRequestProducer.getPersonnelIdAndCompanyIdFromPersonnelService(authId);
        String companyId = responseModel.getCompanyId();
        Advance advance = findById(dto.getRequestId());
        if(!advance.getCompanyId().equals(companyId)) throw new AdvanceServiceException(ErrorType.INVALID_SUPERVISOR);
        if(!advance.getRequestStatus().equals(ERequestStatus.PENDING)) throw new AdvanceServiceException(ErrorType.INVALID_PARAMETER);
        advance.setRequestStatus(ERequestStatus.valueOf(dto.getDecision()));
        update(advance);
        sendDayOffStatusChangeNotificationToPersonnelService(advance);
        return "Operation completed successfully";
    }

    private void sendDayOffStatusChangeNotificationToPersonnelService(Advance advance) {
        SendAdvanceStatusChangeNotificationModel requestModel = SendAdvanceStatusChangeNotificationModel.builder()
                .personnelId(advance.getPersonnelId())
                .requestReason(advance.getReason().toString())
                .requestDescription(advance.getDescription())
                .requestAmount(advance.getAmount())
                .updatedStatus(advance.getRequestStatus().toString())
                .requestCreatedAt(advance.getCreatedAt())
                .requestUpdatedAt(advance.getUpdatedAt())
                .build();
        sendAdvanceStatusChangeNotificationProducer.sendNotificationToPersonnelService(requestModel);
    }

    public List<AdvanceGetAllRequestsResponseDto> getAllRequestsForSupervisor(String token) {
        String authId = validateUserRoleAndRetrieveAuthId(token, SUPERVISOR_ROLE);
        GetPersonnelIdAndCompanyIdForAdvanceRequestModel responseModel = getPersonnelIdAndCompanyIdForAdvanceRequestProducer.getPersonnelIdAndCompanyIdFromPersonnelService(authId);
        String companyId = responseModel.getCompanyId();
        List<Advance> advanceRequests = advanceRepository.findAllByCompanyId(companyId);
        if(advanceRequests.isEmpty()) return Collections.emptyList();
        List<String> personnelIds = advanceRequests.stream().map(Advance::getPersonnelId).toList();
        List<GetPersonnelDetailsForAdvanceRequestModel> responseModelList = getPersonnelDetailsForAdvanceRequestProducer.getPersonnelDetailsFromPersonnelService(personnelIds);
        return prepareGetAllRequestsResponseDto(advanceRequests, responseModelList);
    }

    private List<AdvanceGetAllRequestsResponseDto> prepareGetAllRequestsResponseDto(List<Advance> advanceRequests, List<GetPersonnelDetailsForAdvanceRequestModel> responseModelList) {
        List<AdvanceGetAllRequestsResponseDto> responseDtoList = new ArrayList<>();
        for (int i = 0; i < advanceRequests.size(); i++) {
            responseDtoList.add(AdvanceGetAllRequestsResponseDto.builder()
                    .id(advanceRequests.get(i).getId())
                    .personnelId(responseModelList.get(i).getPersonnelId())
                    .name(responseModelList.get(i).getName())
                    .lastName(responseModelList.get(i).getLastName())
                    .image(responseModelList.get(i).getImage())
                    .email(responseModelList.get(i).getEmail())
                    .advanceQuota(responseModelList.get(i).getAdvanceQuota())
                    .reason(advanceRequests.get(i).getReason().toString())
                    .description(advanceRequests.get(i).getDescription())
                    .amount(advanceRequests.get(i).getAmount())
                    .requestStatus(advanceRequests.get(i).getRequestStatus().toString())
                    .createdAt(Instant.ofEpochMilli(advanceRequests.get(i).getCreatedAt())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate())
                    .updatedAt(Instant.ofEpochMilli(advanceRequests.get(i).getUpdatedAt())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate())
                    .build());
        }
        return responseDtoList;
    }

    public List<AdvanceGetAllMyRequestsResponseDto> getAllMyRequestsForPersonnel(String token) {
        String authId = validateUserRoleAndRetrieveAuthId(token, PERSONNEL_ROLE);
        GetPersonnelIdAndCompanyIdForAdvanceRequestModel responseModel = getPersonnelIdAndCompanyIdForAdvanceRequestProducer.getPersonnelIdAndCompanyIdFromPersonnelService(authId);
        String personnelId = responseModel.getPersonnelId();
        List<Advance> personnelAdvanceRequests = advanceRepository.findAllByPersonnelId(personnelId);
        return prepareGetAllMyRequestsResponseDto(personnelAdvanceRequests);
    }

    private List<AdvanceGetAllMyRequestsResponseDto> prepareGetAllMyRequestsResponseDto(List<Advance> personnelAdvanceRequests) {
        List<AdvanceGetAllMyRequestsResponseDto> responseDtoList = new ArrayList<>();
        for (Advance eachAdvanceRequest : personnelAdvanceRequests) {
            responseDtoList.add(AdvanceGetAllMyRequestsResponseDto.builder()
                    .id(eachAdvanceRequest.getId())
                    .reason(eachAdvanceRequest.getReason().toString())
                    .description(eachAdvanceRequest.getDescription())
                    .amount(eachAdvanceRequest.getAmount())
                    .requestStatus(eachAdvanceRequest.getRequestStatus().toString())
                    .createdAt(Instant.ofEpochMilli(eachAdvanceRequest.getCreatedAt())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate())
                    .updatedAt(Instant.ofEpochMilli(eachAdvanceRequest.getUpdatedAt())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate())
                    .build());
        }
        return responseDtoList;
    }
}
