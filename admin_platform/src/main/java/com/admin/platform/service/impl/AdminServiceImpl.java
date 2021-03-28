package com.admin.platform.service.impl;

import com.admin.platform.model.Admin;
import com.admin.platform.repository.AdminRepository;
import com.admin.platform.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }
}
