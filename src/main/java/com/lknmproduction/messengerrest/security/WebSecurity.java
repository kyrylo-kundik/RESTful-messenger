package com.lknmproduction.messengerrest.security;

import com.lknmproduction.messengerrest.security.filters.JWTAuthenticationFilter;
import com.lknmproduction.messengerrest.security.filters.JWTAuthorizationFilter;
import com.lknmproduction.messengerrest.service.impl.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.lknmproduction.messengerrest.security.SecurityConstants.*;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    public WebSecurity(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
                .antMatchers(HttpMethod.POST, CONFIRM_LOGIN_URL).permitAll()
                .antMatchers(HttpMethod.POST, CREATE_USER_URL).permitAll()
                .antMatchers(HttpMethod.POST, RESEND_CODE_URL).permitAll()
                .antMatchers("/api/v1/user**/**").authenticated()
                .antMatchers("/api/v1/redis**/**").authenticated()
                .antMatchers("/api/v1/chat**/**").authenticated()
                .antMatchers("/api/v1/devices**/**").authenticated()
                .antMatchers("/api/v1/message**/**").authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

}
