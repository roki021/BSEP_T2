package com.admin.platform.service;

import com.admin.platform.dto.LoginDTO;
import com.admin.platform.dto.UserTokenStateDTO;

public interface AuthService {
    UserTokenStateDTO loginUser(LoginDTO credentials);
}
