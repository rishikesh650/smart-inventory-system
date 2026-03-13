package com.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public static PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests((authorize) -> authorize
                                                .requestMatchers(new AntPathRequestMatcher("/debug/ping")).permitAll()
                                                .requestMatchers(new AntPathRequestMatcher("/error")).permitAll()
                                                .requestMatchers(new AntPathRequestMatcher("/register")).permitAll()
                                                .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                                                .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                                                .requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()
                                                .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
                                                .requestMatchers(new AntPathRequestMatcher("/admin/**"))
                                                .hasRole("ADMIN")
                                                .requestMatchers(new AntPathRequestMatcher("/products/**"))
                                                .hasAnyRole("ADMIN", "MANAGER")
                                                .requestMatchers(new AntPathRequestMatcher("/sales/**"))
                                                .hasAnyRole("ADMIN", "MANAGER", "STAFF")
                                                .requestMatchers(new AntPathRequestMatcher("/suppliers/**"))
                                                .hasAnyRole("ADMIN", "MANAGER")
                                                .requestMatchers(new AntPathRequestMatcher("/reports/**"))
                                                .hasAnyRole("ADMIN", "MANAGER", "STAFF")
                                                .anyRequest().authenticated())
                                .formLogin(
                                                form -> form
                                                                .loginPage("/login")
                                                                .loginProcessingUrl("/login")
                                                                .defaultSuccessUrl("/dashboard")
                                                                .permitAll())
                                .logout(
                                                logout -> logout
                                                                .logoutRequestMatcher(
                                                                                new AntPathRequestMatcher("/logout"))
                                                                .permitAll());
                return http.build();
        }
}
