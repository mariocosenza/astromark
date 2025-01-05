package it.astromark.authentication.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import it.astromark.user.commons.model.Role;
import it.astromark.user.commons.model.SchoolUser;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
@NoArgsConstructor
public class JWTService {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private final List<String> blacklist = new ArrayList<>();


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
        return claims.get("role", String.class);
    }


    /**
     * Validates the JWT token against the provided user.
     *
     * @param jwtToken   the JWT token
     * @param schoolUser the user to validate against
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String jwtToken, SchoolUser schoolUser) {
        var tokenUUID = extractUUID(jwtToken);
        var tokenRole = extractRole(jwtToken);
        var claims = extractAllClaims(jwtToken);


        if (blacklist.contains(jwtToken)) {
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
        return !claims.getExpiration().before(new Date());
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param jwtToken the JWT token
     * @return the claims extracted from the token
     */
    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }


    public void logOut(String jwtToken) {
        blacklist.add(jwtToken);

    }


}
