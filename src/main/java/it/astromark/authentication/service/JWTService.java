package it.astromark.authentication.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import it.astromark.authentication.entity.BlackListedToken;
import it.astromark.authentication.repository.BlackListedTokenRepository;
import it.astromark.user.commons.model.Role;
import it.astromark.user.commons.model.SchoolUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@Service

public class JWTService {

    private final BlackListedTokenRepository blackListedTokenRepository;
    @Value("${spring.jwt.secret}")
    private String secretKey;

    public JWTService(final BlackListedTokenRepository blackListedTokenRepository) {
        this.blackListedTokenRepository = blackListedTokenRepository;
    }

    public String generateToken(UUID id, GrantedAuthority role) {

        var claims = new HashMap<String, Object>();
        claims.put("role", role.getAuthority());

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(id.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000))
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] encodedKey = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(encodedKey);
    }

    /**
     * Extracts the UUID (subject) from the given JWT token.
     *
     * @param jwtToken the JWT token
     * @return the extracted UUID
     */
    public UUID extractUUID(String jwtToken) {
        var claims = extractAllClaims(jwtToken);
        if(claims == null) {
            return null;
        }
        return UUID.fromString(claims.getSubject());
    }

    /**
     * Extracts the role from the given JWT token.
     *
     * @param jwtToken the JWT token
     * @return the extracted role as a String
     */
    public String extractRole(String jwtToken) {
        var claims = extractAllClaims(jwtToken);
        return claims != null ? claims.get("role", String.class) : null;
    }


    /**
     * Validates the JWT token against the provided user.
     *
     * @param jwtToken   the JWT token
     * @param schoolUser the user to validate against
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String jwtToken, SchoolUser schoolUser) throws AccessDeniedException {
        var tokenUUID = extractUUID(jwtToken);
        var tokenRole = extractRole(jwtToken);
        var claims = extractAllClaims(jwtToken);


        if (blackListedTokenRepository.existsByToken(jwtToken)) {
            return false;
        }

        // Ensure the token's subject matches the user's ID
        if (!tokenUUID.equals(schoolUser.getId())) {
            return false;
        }

        // Ensure the token's role matches the user's role
        if (!tokenRole.equalsIgnoreCase(Role.getRole(schoolUser))) {
            return false;
        }

        // Ensure the token is not expired
        return !(claims != null && claims.getExpiration().before(new Date()));
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param jwtToken the JWT token
     * @return the claims extracted from the token
     */
    private Claims extractAllClaims(String jwtToken) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload();
        } catch (Exception e) {
            log.warn("Error while extracting claims from JWT token: {}", e.getMessage());
           return null;
        }
    }


    public void logOut(String jwtToken) {
        blackListedTokenRepository.save(new BlackListedToken(jwtToken));
    }


}
