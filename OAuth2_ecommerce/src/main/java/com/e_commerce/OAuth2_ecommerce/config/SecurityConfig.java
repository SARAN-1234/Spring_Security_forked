package com.e_commerce.OAuth2_ecommerce.config;
import com.e_commerce.OAuth2_ecommerce.auth.JwtService;
import com.e_commerce.OAuth2_ecommerce.auth.Jwtfilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.e_commerce.OAuth2_ecommerce.user.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfig {
    @Autowired
    MyUserDetailService myUserDetailService;
    @Autowired
    Jwtfilter jwtfilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.csrf(c->c.disable()).
                authorizeHttpRequests(r->r.requestMatchers("/api/auth/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**")
                        .permitAll().anyRequest().authenticated())
                .authenticationProvider(authenticationProvider()).
                sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class).
                build();

    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
    @Bean
   public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider(myUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
}
