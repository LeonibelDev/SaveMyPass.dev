package com.dac.passwordmanager.config.security;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityFilterConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/signup",
                                "/home",
                                "/",
                                "/about",
                                "/logout",
                                "/add",
                                "/delete/**",
                                "/update/**",
                                "/recovery",
                                "/export",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/profile")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .logout(logout -> logout
                        .logoutUrl("/logout").logoutSuccessUrl("/login").deleteCookies("token")
                        .invalidateHttpSession(true));

        return http.build();
    }
}
