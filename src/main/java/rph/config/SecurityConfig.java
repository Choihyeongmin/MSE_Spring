package rph.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import rph.jwt.JwtAuthenticationFilter;
import rph.jwt.JwtTokenProvider;
import rph.security.CustomUserDetailsService;

@Configuration
@EnableMethodSecurity  // Help role base securtiy. 
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
                .antMatchers("/user/login", "/user/signup", "/user/signup/**","/admin/*.html","/admin/js/*.js","/admin/css/*.css","/user/google/**").permitAll().anyRequest().authenticated()
            .and()
            .addFilterBefore(
                new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
