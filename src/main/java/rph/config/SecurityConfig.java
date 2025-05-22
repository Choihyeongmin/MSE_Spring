package rph.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import rph.jwt.JwtAuthenticationFilter;
import rph.jwt.JwtTokenProvider;
import rph.security.CustomUserDetailsService;

@Configuration
@EnableMethodSecurity  // Role 기반 securtiy를 도와줌. 
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider,
                          CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests()
                .antMatchers("/api/user/login", "/api/user/signup", "/api/user/signup/**","/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .addFilterBefore(
                new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
