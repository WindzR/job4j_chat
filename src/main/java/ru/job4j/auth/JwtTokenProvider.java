package ru.job4j.auth;

import com.auth0.jwt.JWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class JwtTokenProvider {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.prefix}")
    private String prefix;

    @Value("${jwt.token.expired}")
    private long expirationTime;

    private UserDetailsServiceImpl userDetails;

    public JwtTokenProvider(UserDetailsServiceImpl userDetails) {
        this.userDetails = userDetails;
    }

    public String createToken(String username) {
        String token =  JWT.create()
                .withSubject(userDetails.loadUserByUsername(username).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(HMAC512(secret.getBytes()));
        System.out.println("Generate TOKEN " + token);
        return token;
    }
}
