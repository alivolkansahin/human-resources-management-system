package org.musketeers.service;

import lombok.RequiredArgsConstructor;
import org.musketeers.entity.Admin;
import org.musketeers.exception.AdminServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

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
}
