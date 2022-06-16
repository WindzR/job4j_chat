package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.auth.JwtTokenProvider;

@Service
public class JwtTokenService {

    private final JwtTokenProvider jwtToken;

    public JwtTokenService(final JwtTokenProvider jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String createToken(String username) {
        return jwtToken.createToken(username);
    }
}
