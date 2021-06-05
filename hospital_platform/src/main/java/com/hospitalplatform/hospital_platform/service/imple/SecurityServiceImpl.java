package com.hospitalplatform.hospital_platform.service.imple;

import com.hospitalplatform.hospital_platform.dto.SecretCommunicationTokenDTO;
import com.hospitalplatform.hospital_platform.models.SecretCommunicationToken;
import com.hospitalplatform.hospital_platform.repository.SecretCommunicationTokenRepository;
import com.hospitalplatform.hospital_platform.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityServiceImpl implements SecurityService {
    @Autowired
    private SecretCommunicationTokenRepository secretCommunicationTokenRepository;

    private static Integer TOKE_ID = 0;

    @Override
    public SecretCommunicationTokenDTO getSecurityToken() {
        Optional<SecretCommunicationToken> token = secretCommunicationTokenRepository.findById(TOKE_ID);

        if (token.isEmpty())
            return null;

        return new SecretCommunicationTokenDTO(token.get().getToken(), token.get().isActive());
    }

    @Override
    public void setSecurityToken(SecretCommunicationTokenDTO token) {
        secretCommunicationTokenRepository.save(new SecretCommunicationToken(token.getToken()));
    }

    @Override
    public boolean checkSecurityToken(String token) {
        Optional<SecretCommunicationToken> tok = secretCommunicationTokenRepository.findById(TOKE_ID);
        if (tok.isEmpty())
            return false;
        return tok.get().isActive() && tok.get().getToken().equals(token);
    }

    @Override
    public void changeSecurityTokenStatus(boolean tokenActive) throws Exception {
        Optional<SecretCommunicationToken> token = secretCommunicationTokenRepository.findById(TOKE_ID);

        if (token.isEmpty())
            throw new Exception("Security token does not exist.");

        SecretCommunicationToken t = token.get();
        t.setActive(tokenActive);
        secretCommunicationTokenRepository.save(t);
    }


}
