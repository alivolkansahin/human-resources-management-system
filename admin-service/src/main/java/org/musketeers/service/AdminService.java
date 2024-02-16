package org.musketeers.service;

import org.musketeers.dto.request.AdminRegisterDto;
import org.musketeers.dto.request.AdminSupervisorRegistrationDecisionRequestDto;
import org.musketeers.dto.response.RegisteredSupervisorsResponseDTO;
import org.musketeers.entity.Admin;
import org.musketeers.exception.AdminServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.mapper.IAdminMapper;
import org.musketeers.rabbitmq.model.RegisterAdminModel;
import org.musketeers.rabbitmq.model.SupervisorRegistrationDecisionModel;
import org.musketeers.rabbitmq.producer.RegisterAdminProducer;
import org.musketeers.rabbitmq.producer.SupervisorRegistrationDecisionProducer;
import org.musketeers.rabbitmq.producer.RegisteredSupervisorsRequestProducer;
import org.musketeers.repository.AdminRepository;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService extends ServiceManager<Admin, String> {

    private final AdminRepository adminRepository;
    private final JwtTokenManager jwtTokenManager;
    private final IAdminMapper adminMapper;
    private final RegisteredSupervisorsRequestProducer registeredSupervisorsRequestProducer;
    private final SupervisorRegistrationDecisionProducer supervisorRegistrationDecisionProducer;
    private final RegisterAdminProducer registerAdminProducer;

    public AdminService(AdminRepository adminRepository, JwtTokenManager jwtTokenManager, IAdminMapper adminMapper, RegisteredSupervisorsRequestProducer registeredSupervisorsRequestProducer, SupervisorRegistrationDecisionProducer supervisorRegistrationDecisionProducer, RegisterAdminProducer registerAdminProducer) {
        super(adminRepository);
        this.adminRepository = adminRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.adminMapper = adminMapper;
        this.registeredSupervisorsRequestProducer = registeredSupervisorsRequestProducer;
        this.supervisorRegistrationDecisionProducer = supervisorRegistrationDecisionProducer;
        this.registerAdminProducer = registerAdminProducer;
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
        String email = dto.getName() + "@admin.com";
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

    public ResponseEntity<List<RegisteredSupervisorsResponseDTO>> getAllRegisteredSupervisors(String adminId) {
        // Volkan: 52-53e gerek yok, company mikroservisindeki servis katmanındaki updateCompany methodunda güzelce açıklamıştım neyin nasıl olacağını, ona göre düzenlenecek buralar...
        //Optional<String> token = jwtTokenManager.createToken(adminId);
        //if (token.isPresent()){
            List<RegisteredSupervisorsResponseDTO> dtoList = registeredSupervisorsRequestProducer.convertSendAndReceive(adminId);
            return ResponseEntity.ok(dtoList);
       // }else {
       //    throw new AdminServiceException(ErrorType.ADMIN_NOT_FOUND);
      // }

    }

    public String handleSupervisorRegistration(AdminSupervisorRegistrationDecisionRequestDto dto) {
        SupervisorRegistrationDecisionModel model = SupervisorRegistrationDecisionModel.builder()
                .supervisorAuthId(dto.getSupervisorAuthId())
                .decision(dto.getDecision().equalsIgnoreCase("true"))
                .build();
        supervisorRegistrationDecisionProducer.sendRegistrationDecision(model);
        return "Success";
    }

}
