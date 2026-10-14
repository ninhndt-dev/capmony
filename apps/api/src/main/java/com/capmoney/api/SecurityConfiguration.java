package com.capmoney.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, IngressFilter ingress) throws Exception {
        // INFRA-01 has no account endpoints. CM-01 supplies CSRF and authorization before product mutations exist.
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .addFilterBefore(ingress, UsernamePasswordAuthenticationFilter.class)
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
