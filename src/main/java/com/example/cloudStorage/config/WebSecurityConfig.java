package com.example.cloudStorage.config;

import com.example.cloudStorage.security.UserDetailsServiceImpl;
import com.example.cloudStorage.security.jwt.JwtAuthenticationEntryPoint;
import com.example.cloudStorage.security.jwt.JwtTokenFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig { //WebSecurityConfigurerAdapter has been deprecated

      JwtTokenFilter jwtTokenFilter;
      JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
      UserDetailsServiceImpl userDetailsServiceImpl;


    @Autowired
    public WebSecurityConfig(JwtTokenFilter jwtTokenFilter,
                             JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                             UserDetailsServiceImpl userDetailsServiceImpl) {
       this.jwtTokenFilter = jwtTokenFilter;
       this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
       this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("api/admin").hasRole("ADMIN")
                .antMatchers("/api/auth/**").permitAll()
                .anyRequest().hasAnyRole("USER")
                .and()
                .formLogin().loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .failureUrl("/auth/login?error")
                .and()
                .logout()
                .logoutUrl("/logout").logoutSuccessUrl("/auth/login")
                .and()
                .sessionManagement()
                //сессия на сервере не хранится
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceImpl);
        daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
