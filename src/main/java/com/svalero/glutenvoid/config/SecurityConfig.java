package com.svalero.glutenvoid.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**","/users/login", "/users", "/auth/refresh").permitAll()
                        .requestMatchers("/users/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .requestMatchers(HttpMethod.GET,"/establishments/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .requestMatchers(HttpMethod.POST,"/establishments").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/establishments/{id}").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH,"/establishments/{id}").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/recipes/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .requestMatchers(HttpMethod.POST, "/recipes").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .requestMatchers(HttpMethod.DELETE, "/recipes/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .requestMatchers(HttpMethod.PATCH, "/recipes/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
