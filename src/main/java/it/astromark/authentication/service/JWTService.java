package it.astromark.authentication.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.astromark.user.commons.model.Role;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class JWTService {

    private final String secretKey;

    public JWTService() {

        try {
            var keyGen = KeyGenerator.getInstance("HmacSHA256");
            var sk = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(UUID id, Role role) {

        var claims = new HashMap<String, Object>();
        claims.put("role", role);

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(id.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 100000))
                .and()
                .signWith(getKey())
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
