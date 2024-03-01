package org.musketeers.service;

import org.musketeers.dto.request.SpendingCancelRequestDto;
import org.musketeers.dto.request.SpendingCreateRequestDto;
import org.musketeers.dto.request.SpendingUpdateRequestDto;
import org.musketeers.dto.response.SpendingGetAllMyRequestsResponseDto;
import org.musketeers.dto.response.SpendingGetAllRequestsResponseDto;
import org.musketeers.entity.Attachment;
import org.musketeers.entity.Spending;
import org.musketeers.entity.enums.ECurrency;
import org.musketeers.entity.enums.EReason;
import org.musketeers.entity.enums.ERequestStatus;
import org.musketeers.exception.ErrorType;
import org.musketeers.exception.SpendingServiceException;
import org.musketeers.rabbitmq.model.GetPersonnelDetailsForSpendingRequestModel;
import org.musketeers.rabbitmq.model.GetPersonnelIdAndCompanyIdForSpendingRequestModel;
import org.musketeers.rabbitmq.model.SendSpendingStatusChangeNotificationModel;
import org.musketeers.rabbitmq.producer.GetPersonnelDetailsForSpendingRequestProducer;
import org.musketeers.rabbitmq.producer.GetPersonnelIdAndCompanyIdForSpendingRequestProducer;
import org.musketeers.rabbitmq.producer.SendSpendingStatusChangeNotificationProducer;
import org.musketeers.repository.SpendingRepository;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpendingService extends ServiceManager<Spending, String> {

    private final SpendingRepository spendingRepository;

    private final JwtTokenManager jwtTokenManager;

//    private final Cloudinary cloudinary;

    private final GetPersonnelIdAndCompanyIdForSpendingRequestProducer getPersonnelIdAndCompanyIdForSpendingRequestProducer;

    private final SendSpendingStatusChangeNotificationProducer sendSpendingStatusChangeNotificationProducer;

    private final GetPersonnelDetailsForSpendingRequestProducer getPersonnelDetailsForSpendingRequestProducer;

    private static final String SUPERVISOR_ROLE = "SUPERVISOR";

    private static final String PERSONNEL_ROLE = "PERSONNEL";

    public SpendingService(SpendingRepository spendingRepository, JwtTokenManager jwtTokenManager, GetPersonnelIdAndCompanyIdForSpendingRequestProducer getPersonnelIdAndCompanyIdForSpendingRequestProducer, SendSpendingStatusChangeNotificationProducer sendSpendingStatusChangeNotificationProducer, GetPersonnelDetailsForSpendingRequestProducer getPersonnelDetailsForSpendingRequestProducer) {
        super(spendingRepository);
        this.spendingRepository = spendingRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.getPersonnelIdAndCompanyIdForSpendingRequestProducer = getPersonnelIdAndCompanyIdForSpendingRequestProducer;
        this.sendSpendingStatusChangeNotificationProducer = sendSpendingStatusChangeNotificationProducer;
        this.getPersonnelDetailsForSpendingRequestProducer = getPersonnelDetailsForSpendingRequestProducer;
    }

    private String validateUserRoleAndRetrieveAuthId(String token, String roleString) {
        List<String> claimsFromToken = jwtTokenManager.getClaimsFromToken(token);
        String role = claimsFromToken.get(1);
        if(!role.equals(roleString)) throw new SpendingServiceException(ErrorType.INVALID_ROLE);
        return claimsFromToken.get(0);
    }

    public String createRequest(SpendingCreateRequestDto dto) {
        String authId = validateUserRoleAndRetrieveAuthId(dto.getToken(), PERSONNEL_ROLE);
        GetPersonnelIdAndCompanyIdForSpendingRequestModel responseModel = getPersonnelIdAndCompanyIdForSpendingRequestProducer.getPersonnelIdAndCompanyIdFromPersonnelService(authId);
        String personnelId = responseModel.getPersonnelId();
        String companyId = responseModel.getCompanyId();
        List<Spending> personnelPendingRequests = spendingRepository.findAllByPersonnelIdAndRequestStatus(personnelId, ERequestStatus.PENDING);
        Spending spending = Spending.builder()
                .personnelId(personnelId)
                .companyId(companyId)
                .reason(EReason.valueOf(dto.getReason()))
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .currency(ECurrency.valueOf(dto.getCurrency()))
                .spendingDate(dto.getSpendingDate())
                .build();
        long time = System.currentTimeMillis();
        spending.setAttachments(dto.getAttachmentUrls().stream()
                        .map(attachmentUrl -> Attachment.builder()
                            .spending(spending)
                            .fileUrl(attachmentUrl)
                            .createdAt(time)
                            .updatedAt(time)
                            .status(true)
                            .build())
                        .collect(Collectors.toList()));
        save(spending);
        return "Request Created!";
    }

    public String cancelRequest(SpendingCancelRequestDto dto) {
        String authId = validateUserRoleAndRetrieveAuthId(dto.getToken(), PERSONNEL_ROLE);
        GetPersonnelIdAndCompanyIdForSpendingRequestModel responseModel = getPersonnelIdAndCompanyIdForSpendingRequestProducer.getPersonnelIdAndCompanyIdFromPersonnelService(authId);
        String personnelId = responseModel.getPersonnelId();
        Spending spending = findById(dto.getRequestId());
        if(!spending.getPersonnelId().equals(personnelId)) throw new SpendingServiceException(ErrorType.INVALID_PERSON);
        if(!spending.getRequestStatus().equals(ERequestStatus.PENDING)) throw new SpendingServiceException(ErrorType.INVALID_PARAMETER);
        spending.setRequestStatus(ERequestStatus.CANCELLED);
        update(spending);
        return "Request Cancelled!";
    }

    public String updateRequestStatus(SpendingUpdateRequestDto dto) {
        String authId = validateUserRoleAndRetrieveAuthId(dto.getToken(), SUPERVISOR_ROLE);
        GetPersonnelIdAndCompanyIdForSpendingRequestModel responseModel = getPersonnelIdAndCompanyIdForSpendingRequestProducer.getPersonnelIdAndCompanyIdFromPersonnelService(authId);
        String companyId = responseModel.getCompanyId();
        Spending spending = findById(dto.getRequestId());
        if(!spending.getCompanyId().equals(companyId)) throw new SpendingServiceException(ErrorType.INVALID_SUPERVISOR);
        if(!spending.getRequestStatus().equals(ERequestStatus.PENDING)) throw new SpendingServiceException(ErrorType.INVALID_PARAMETER);
        spending.setRequestStatus(ERequestStatus.valueOf(dto.getDecision()));
        update(spending);
        sendSpendingStatusChangeNotificationToPersonnelService(spending);
        return "Operation completed successfully";
    }

    private void sendSpendingStatusChangeNotificationToPersonnelService(Spending spending) {
        SendSpendingStatusChangeNotificationModel requestModel = SendSpendingStatusChangeNotificationModel.builder()
                .personnelId(spending.getPersonnelId())
                .requestReason(spending.getReason().toString())
                .requestDescription(spending.getDescription())
                .requestAmount(spending.getAmount())
                .requestCurrency(spending.getCurrency().toString())
                .requestSpendingDate(spending.getSpendingDate())
                .requestAttachments(spending.getAttachments().stream().map(Attachment::getFileUrl).toList())
                .updatedStatus(spending.getRequestStatus().toString())
                .requestCreatedAt(spending.getCreatedAt())
                .requestUpdatedAt(spending.getUpdatedAt())
                .build();
        sendSpendingStatusChangeNotificationProducer.sendNotificationToPersonnelService(requestModel);
    }

    public List<SpendingGetAllRequestsResponseDto> getAllRequestsForSupervisor(String token) {
        String authId = validateUserRoleAndRetrieveAuthId(token, SUPERVISOR_ROLE);
        GetPersonnelIdAndCompanyIdForSpendingRequestModel responseModel = getPersonnelIdAndCompanyIdForSpendingRequestProducer.getPersonnelIdAndCompanyIdFromPersonnelService(authId);
        String companyId = responseModel.getCompanyId();
        List<Spending> spendingRequests = spendingRepository.findAllByCompanyId(companyId);
        if(spendingRequests.isEmpty()) return Collections.emptyList();
        List<String> personnelIds = spendingRequests.stream().map(Spending::getPersonnelId).toList();
        List<GetPersonnelDetailsForSpendingRequestModel> responseModelList = getPersonnelDetailsForSpendingRequestProducer.getPersonnelDetailsFromPersonnelService(personnelIds);
        return prepareGetAllRequestsResponseDto(spendingRequests, responseModelList);
    }

    private List<SpendingGetAllRequestsResponseDto> prepareGetAllRequestsResponseDto(List<Spending> spendingRequests, List<GetPersonnelDetailsForSpendingRequestModel> responseModelList) {
        List<SpendingGetAllRequestsResponseDto> responseDtoList = new ArrayList<>();
        for (int i = 0; i < spendingRequests.size(); i++) {
            responseDtoList.add(SpendingGetAllRequestsResponseDto.builder()
                    .id(spendingRequests.get(i).getId())
                    .personnelId(responseModelList.get(i).getPersonnelId())
                    .name(responseModelList.get(i).getName())
                    .lastName(responseModelList.get(i).getLastName())
                    .image(responseModelList.get(i).getImage())
                    .email(responseModelList.get(i).getEmail())
                    .reason(spendingRequests.get(i).getReason().toString())
                    .description(spendingRequests.get(i).getDescription())
                    .amount(spendingRequests.get(i).getAmount())
                    .currency(spendingRequests.get(i).getCurrency().toString())
                    .spendingDate(spendingRequests.get(i).getSpendingDate())
                    .attachments(spendingRequests.get(i).getAttachments().stream().map(Attachment::getFileUrl).toList())
                    .requestStatus(spendingRequests.get(i).getRequestStatus().toString())
                    .createdAt(Instant.ofEpochMilli(spendingRequests.get(i).getCreatedAt())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate())
                    .updatedAt(Instant.ofEpochMilli(spendingRequests.get(i).getUpdatedAt())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate())
                    .build());
        }
        return responseDtoList;
    }

    public List<SpendingGetAllMyRequestsResponseDto> getAllMyRequestsForPersonnel(String token) {
        String authId = validateUserRoleAndRetrieveAuthId(token, PERSONNEL_ROLE);
        GetPersonnelIdAndCompanyIdForSpendingRequestModel responseModel = getPersonnelIdAndCompanyIdForSpendingRequestProducer.getPersonnelIdAndCompanyIdFromPersonnelService(authId);
        String personnelId = responseModel.getPersonnelId();
        List<Spending> personnelSpendingRequests = spendingRepository.findAllByPersonnelId(personnelId);
        return prepareGetAllMyRequestsResponseDto(personnelSpendingRequests);
    }

    private List<SpendingGetAllMyRequestsResponseDto> prepareGetAllMyRequestsResponseDto(List<Spending> personnelSpendingRequests) {
        List<SpendingGetAllMyRequestsResponseDto> responseDtoList = new ArrayList<>();
        for (Spending eachSpendingRequest : personnelSpendingRequests) {
            responseDtoList.add(SpendingGetAllMyRequestsResponseDto.builder()
                    .id(eachSpendingRequest.getId())
                    .reason(eachSpendingRequest.getReason().toString())
                    .description(eachSpendingRequest.getDescription())
                    .amount(eachSpendingRequest.getAmount())
                    .currency(eachSpendingRequest.getCurrency().toString())
                    .spendingDate(eachSpendingRequest.getSpendingDate())
                    .attachments(eachSpendingRequest.getAttachments().stream().map(Attachment::getFileUrl).toList())
                    .requestStatus(eachSpendingRequest.getRequestStatus().toString())
                    .createdAt(Instant.ofEpochMilli(eachSpendingRequest.getCreatedAt())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate())
                    .updatedAt(Instant.ofEpochMilli(eachSpendingRequest.getUpdatedAt())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate())
                    .build());
        }
        return responseDtoList;
    }
}
