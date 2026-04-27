package com.example.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public UserDetailsService users() {
    UserDetails user = User.withUsername("user").password("password").roles("USER").build();

    return new InMemoryUserDetailsManager(user);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            auth -> auth.requestMatchers("/css/**").permitAll().anyRequest().authenticated())
        .formLogin(form -> form.defaultSuccessUrl("/orders", true))
        .logout(logout -> logout.logoutSuccessUrl("/login"));

    return http.build();
  }
}
