package org.musketeers.service;

import lombok.RequiredArgsConstructor;
import org.musketeers.dto.response.RegisteredSupervisorsResponseDTO;
import org.musketeers.entity.Admin;
import org.musketeers.exception.AdminServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.mapper.IAdminMapper;
import org.musketeers.rabbitmq.model.SupervisorModel;
import org.musketeers.rabbitmq.producer.RegisteredSupervisorsRequestProducer;
import org.musketeers.repository.AdminRepository;
import org.musketeers.utility.JwtTokenManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final RegisteredSupervisorsRequestProducer registeredSupervisorsRequestProducer;
    private final JwtTokenManager jwtTokenManager;
    private final IAdminMapper adminMapper;
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

    public Admin register(Admin admin) {
        Long time = System.currentTimeMillis();
        admin.setCreatedAt(time);
        admin.setUpdatedAt(time);
        admin.setStatus(true);
        return adminRepository.save(admin);
    }

    public ResponseEntity<List<RegisteredSupervisorsResponseDTO>> getAllRegisteredSupervisors(String adminId) {
        Optional<String> token = jwtTokenManager.createToken(adminId);
        List<RegisteredSupervisorsResponseDTO> responseDTOS = new ArrayList<>();
        if (token.isPresent()){
            List<SupervisorModel> modelList = registeredSupervisorsRequestProducer.convertSendAndReceive(token.get());
            modelList.forEach((model)-> {
                RegisteredSupervisorsResponseDTO registeredSupervisorsResponseDTO = adminMapper.supervisorModelToDto(model);
                responseDTOS.add(registeredSupervisorsResponseDTO);
            });
            return ResponseEntity.ok(responseDTOS);
        }else {
            throw new AdminServiceException(ErrorType.ADMIN_NOT_FOUND);
        }

    }
}
