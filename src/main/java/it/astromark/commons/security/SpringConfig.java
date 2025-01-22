package it.astromark.commons.security;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringConfig implements WebMvcConfigurer {


    private final JwtFilter jwtFilter;

    @Autowired
    public SpringConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }


    /**
     * Temporary bean to disable default http basic auth
     *
     * @param http incoming request
     * @return SecurityFilterChain bean
     * @throws Exception generic exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/auth/login", "/api/auth/first-login").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/", "/login").permitAll() // Grouped for conciseness
                        .anyRequest().permitAll() // Important: Allow everything else
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    /**
     * Disable CORS for frontend page served in static folder
     *
     * @param registry map for cors policy
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * Add resource handler to serve web pages in static folder
     *
     * @param registry map for cors policy
     */
    @Override
    public void addResourceHandlers(@Nullable ResourceHandlerRegistry registry) {
        this.serveDirectory(Objects.requireNonNull(registry));
    }


    private void serveDirectory(ResourceHandlerRegistry registry) {
        String[] endpointPatterns = new String[]{"/".substring(0, 0), "/", "/" + "**"};
        registry
                .addResourceHandler(endpointPatterns)
                .addResourceLocations("classpath:/static/")
                .resourceChain(false)
                .addResolver(new PathResourceResolver() {
                    @Override
                    public Resource resolveResource(HttpServletRequest request, @Nullable String requestPath, @Nullable List<? extends Resource> locations, @Nullable ResourceResolverChain chain) {
                        Resource resource = super.resolveResource(request, Objects.requireNonNull(requestPath), Objects.requireNonNull(locations), Objects.requireNonNull(chain));
                        if (Objects.nonNull(resource)) {
                            return resource;
                        }
                        return super.resolveResource(request, "/index.html", locations, chain);
                    }
                });
    }

}