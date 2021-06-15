package com.hospitalplatform.hospital_platform.service.imple;

import com.hospitalplatform.hospital_platform.dto.IntermediateToken;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtils tokenUtils;

    public IntermediateToken loginUser(LoginDTO credentials) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(),
                        credentials.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String fingerprint = generateFingerprint();

        HospitalUser user = (HospitalUser) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getUsername(),
                ((SimpleGrantedAuthority)user.getAuthorities().toArray()[0]).getAuthority(),
                hashedFingerprint(fingerprint));
        int expiredIn = tokenUtils.getExpiredIn();

        return new IntermediateToken(
                new UserTokenStateDTO(jwt, expiredIn),
                fingerprint);
    }

    private String generateFingerprint() {
        SecureRandom secureRandom = new SecureRandom();

        byte[] randomFgp = new byte[50];
        secureRandom.nextBytes(randomFgp);

        return DatatypeConverter.printHexBinary(randomFgp);
    }

    private String hashedFingerprint(String fingerprint) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] userFingerprintDigest = digest.digest(fingerprint.getBytes("utf-8"));
        return DatatypeConverter.printHexBinary(userFingerprintDigest);
    }
}
