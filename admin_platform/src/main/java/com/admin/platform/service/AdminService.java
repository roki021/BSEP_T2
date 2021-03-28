package com.admin.platform.service;

import com.admin.platform.model.Admin;

public interface AdminService {
    Admin findByUsername(String username);
}
