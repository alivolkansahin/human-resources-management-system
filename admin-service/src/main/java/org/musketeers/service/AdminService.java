package org.musketeers.service;

import lombok.RequiredArgsConstructor;
import org.musketeers.dto.response.RegisteredSupervisorsResponseDTO;
import org.musketeers.entity.Admin;
import org.musketeers.exception.AdminServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.mapper.IAdminMapper;
import org.musketeers.rabbitmq.producer.RegisteredSupervisorsRequestProducer;
import org.musketeers.repository.AdminRepository;

import org.musketeers.utility.JwtTokenManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
        // Volkan: 49-50ye gerek yok, company mikroservisindeki servis katmanındaki updateCompany methodunda güzelce açıklamıştım neyin nasıl olacağını, ona göre düzenlenecek buralar...
        //Optional<String> token = jwtTokenManager.createToken(adminId);
        //if (token.isPresent()){
            List<RegisteredSupervisorsResponseDTO> dtoList = registeredSupervisorsRequestProducer.convertSendAndReceive(adminId);
            return ResponseEntity.ok(dtoList);
       // }else {
       //    throw new AdminServiceException(ErrorType.ADMIN_NOT_FOUND);
      // }

    }
}
