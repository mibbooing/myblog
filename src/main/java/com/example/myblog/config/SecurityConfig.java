package com.example.myblog.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin()
                        .loginPage("/members/login")
                        .defaultSuccessUrl("/")
                        .successHandler(customAuthenticationSuccessHandler)
                        .usernameParameter("email")
                        .loginProcessingUrl("/members/login")
                        .failureUrl("/members/login/error")
                     .and()
                        .logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                        .logoutSuccessUrl("/");
        http.authorizeRequests()
                .mvcMatchers("/", "/home","/members/login","/members/login/error","/members/new","/errors/*","/blogs/main/**","/blogs/category/**","/posts/details/*","/images/**").permitAll()
                .anyRequest().authenticated();
        http.exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/img/**", "/js/**", "/css/**");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
    }
}
