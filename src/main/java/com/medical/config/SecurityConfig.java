package com.medical.config;

import com.medical.service.DoctorService;
import com.medical.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public DaoAuthenticationProvider doctorAuthProvider(DoctorService doctorService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(doctorService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public DaoAuthenticationProvider patientAuthProvider(PatientService patientService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(patientService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            DaoAuthenticationProvider doctorAuthProvider,
            DaoAuthenticationProvider patientAuthProvider) {
        return new ProviderManager(doctorAuthProvider, patientAuthProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, 
            DaoAuthenticationProvider doctorAuthProvider,
            DaoAuthenticationProvider patientAuthProvider) throws Exception {
        http
            .authenticationProvider(doctorAuthProvider)
            .authenticationProvider(patientAuthProvider)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/home", "/about", "/contact", "/specialty","/doctor","/booking", "/search/**").permitAll()
                .requestMatchers("/css/**", "/js/**", "/img/**", "/images/**", "/webjars/**").permitAll()
                .requestMatchers("/register/**", "/login", "/error").permitAll()
//                .requestMatchers("/doctor/**").hasRole("DOCTOR")
                .requestMatchers("/patient/**").hasRole("PATIENT")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }
} 