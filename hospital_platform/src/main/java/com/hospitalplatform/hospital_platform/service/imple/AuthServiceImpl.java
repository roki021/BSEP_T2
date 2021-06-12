package com.hospitalplatform.hospital_platform.service.imple;

import com.hospitalplatform.hospital_platform.dto.LoginDTO;
import com.hospitalplatform.hospital_platform.dto.UserTokenStateDTO;
import com.hospitalplatform.hospital_platform.models.HospitalUser;
import com.hospitalplatform.hospital_platform.security.TokenUtils;
import com.hospitalplatform.hospital_platform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
        Authentication authentication = null;
        try {
           authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(),
                            credentials.getPassword()));
        } catch (AuthenticationException e) {
            return null;
        }


        SecurityContextHolder.getContext().setAuthentication(authentication);

        HospitalUser user = (HospitalUser) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiredIn = tokenUtils.getExpiredIn();

        return new UserTokenStateDTO(jwt, expiredIn);
    }
}
