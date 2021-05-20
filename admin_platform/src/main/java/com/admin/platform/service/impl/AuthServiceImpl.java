package com.admin.platform.service.impl;

import com.admin.platform.dto.LoginDTO;
import com.admin.platform.dto.UserTokenStateDTO;
import com.admin.platform.model.Admin;
import com.admin.platform.security.TokenUtils;
import com.admin.platform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public UserTokenStateDTO loginUser(LoginDTO credentials) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(),
                        credentials.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Admin user = (Admin) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiredIn = tokenUtils.getExpiredIn();

        return new UserTokenStateDTO(jwt, expiredIn);
    }
}
