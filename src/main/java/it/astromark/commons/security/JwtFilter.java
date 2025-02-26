package it.astromark.commons.security;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.authentication.service.JWTService;
import it.astromark.user.commons.model.SchoolUser;
import jakarta.annotation.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final AuthenticationService authenticationService;

    @Autowired
    public JwtFilter(JWTService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable FilterChain filterChain) throws ServletException, IOException {
        var authorizationHeader = request.getHeader("Authorization");
        String jwtToken = null;
        UUID id = null;
        var role = "";
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            id = jwtService.extractUUID(jwtToken);
            role = jwtService.extractRole(jwtToken);

        }
        if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            SchoolUser schoolUser = authenticationService.getUser(id, role);
            try {
                if (jwtService.validateToken(jwtToken, schoolUser)) {
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(schoolUser, null, List.of(authenticationService.getRole(schoolUser)));
                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            } catch (Exception e) {
                logger.error("Error while validating token", e);
            }

        }
        Objects.requireNonNull(filterChain).doFilter(request, response);
    }
}
