package org.musketeers.service;

import org.musketeers.dto.request.AdminHandleCommentDecisionRequestDto;
import org.musketeers.dto.request.AdminRegisterDto;
import org.musketeers.dto.request.AdminSupervisorRegistrationDecisionRequestDto;
import org.musketeers.dto.response.GetAllPendingCommentsResponseDto;
import org.musketeers.dto.response.RegisteredSupervisorsResponseDTO;
import org.musketeers.entity.Admin;
import org.musketeers.exception.AdminServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.mapper.IAdminMapper;
import org.musketeers.rabbitmq.model.GetAllPendingCommentsResponseModel;
import org.musketeers.rabbitmq.model.GetSupervisorResponseModel;
import org.musketeers.rabbitmq.model.RegisterAdminModel;
import org.musketeers.rabbitmq.model.SupervisorRegistrationDecisionModel;
import org.musketeers.rabbitmq.producer.*;
import org.musketeers.repository.AdminRepository;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class AdminService extends ServiceManager<Admin, String> {

    private final AdminRepository adminRepository;
    private final JwtTokenManager jwtTokenManager;
    private final RegisteredSupervisorsRequestProducer registeredSupervisorsRequestProducer;
    private final SupervisorRegistrationDecisionProducer supervisorRegistrationDecisionProducer;
    private final RegisterAdminProducer registerAdminProducer;
    private final GetAllPendingCommentsRequestProducer getAllPendingCommentsRequestProducer;

    private final CommentDecisionProducer commentDecisionProducer;

    public AdminService(AdminRepository adminRepository, JwtTokenManager jwtTokenManager, RegisteredSupervisorsRequestProducer registeredSupervisorsRequestProducer, SupervisorRegistrationDecisionProducer supervisorRegistrationDecisionProducer, RegisterAdminProducer registerAdminProducer, GetAllPendingCommentsRequestProducer getAllPendingCommentsRequestProducer, CommentDecisionProducer commentDecisionProducer) {
        super(adminRepository);
        this.adminRepository = adminRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.registeredSupervisorsRequestProducer = registeredSupervisorsRequestProducer;
        this.supervisorRegistrationDecisionProducer = supervisorRegistrationDecisionProducer;
        this.registerAdminProducer = registerAdminProducer;
        this.getAllPendingCommentsRequestProducer = getAllPendingCommentsRequestProducer;
        this.commentDecisionProducer = commentDecisionProducer;
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminById(String id) {
        return adminRepository.findById(id).orElseThrow(() -> new AdminServiceException(ErrorType.ADMIN_NOT_FOUND));
    }

    public Admin update(Admin admin) {
        admin.setUpdatedAt(System.currentTimeMillis());
        return adminRepository.save(admin);
    }

    public Boolean register(AdminRegisterDto dto) {
        String email = dto.getName().toLowerCase(Locale.ROOT) + "@admin.com";
        RegisterAdminModel model = RegisterAdminModel.builder()
                .email(email)
                .phone(dto.getPhoneNumber())
                .password(dto.getPassword())
                .build();
        String authId = registerAdminProducer.getAuthId(model);
        Admin admin = Admin.builder()
                .authId(authId)
                .name(dto.getName())
                .lastName(dto.getLastName())
                .email(email)
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .build();
        save(admin);
        return true;
    }

    public ResponseEntity<List<RegisteredSupervisorsResponseDTO>> getAllPendingSupervisors() {
        List<GetSupervisorResponseModel> getSupervisorResponseModels = registeredSupervisorsRequestProducer.convertSendAndReceive();
        List<RegisteredSupervisorsResponseDTO> dtoList = new ArrayList<>();
        getSupervisorResponseModels.forEach((model) -> {
            dtoList.add(IAdminMapper.INSTANCE.supervisorModelToDto(model));
        });
        return ResponseEntity.ok(dtoList);
    }

    public String handleSupervisorRegistration(AdminSupervisorRegistrationDecisionRequestDto dto) {
        SupervisorRegistrationDecisionModel model = SupervisorRegistrationDecisionModel.builder()
                .supervisorAuthId(dto.getSupervisorAuthId())
                .decision(dto.getDecision().equalsIgnoreCase("true"))
                .build();
        supervisorRegistrationDecisionProducer.sendRegistrationDecision(model);
        return "Success";
    }

    public List<GetAllPendingCommentsResponseDto> getAllPendingComments() {
        List<GetAllPendingCommentsResponseModel> pendingCommentsModel = getAllPendingCommentsRequestProducer.getPendingComments();
        return pendingCommentsModel.stream().map(IAdminMapper.INSTANCE::getAllPendingCommentsModelToDto).toList();
    }

    public Boolean handleCommentDecision(AdminHandleCommentDecisionRequestDto dto) {
        commentDecisionProducer.sendCommentDecision(IAdminMapper.INSTANCE.commentDecisionDtoToModel(dto));
        return true;
    }
}
