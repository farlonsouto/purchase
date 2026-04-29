package com.example.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  // Better to explicitly encode and avoid the {noop} workaround
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * For convenience, this method defines an in-memory user store with a single user having the
   * username "user" and the password "password". The password is encoded using the provided {@link
   * PasswordEncoder} bean. The user is assigned the role "USER".
   */
  @Bean
  public UserDetailsService users(PasswordEncoder encoder) {
    // TODO to be removed ASAP
    UserDetails user =
        User.withUsername("user").password(encoder.encode("password")).roles("USER").build();

    return new InMemoryUserDetailsManager(user);
  }

  /**
   * /** * Configures the application's HTTP security rules. * *
   *
   * <p>This filter chain applies the following behavior: * *
   *
   * <ul>
   *   *
   *   <li>Allows unauthenticated access to static CSS resources under {@code /css/**} * and to the
   *       login page at {@code /login}. *
   *   <li>Restricts access to order-view pages under {@code /view/orders/**} * to users with the
   *       {@code USER} role. *
   *   <li>Requires authentication for every other request. *
   *   <li>Enables form-based login and redirects successfully authenticated users * to {@code
   *       /view/orders}. *
   *   <li>Configures logout to redirect users back to the login page. *
   * </ul>
   *
   * @param http the {@link HttpSecurity} builder used to define web-based security * @return the
   *     configured {@link SecurityFilterChain} instance * @t hrows Exception if the security
   *     configuration cannot be built
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/css/**", "/login")
                    .permitAll()
                    .requestMatchers("/view/orders/**")
                    .hasRole("USER")
                    .anyRequest()
                    .authenticated())
        .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/view/orders"))
        .logout(logout -> logout.logoutSuccessUrl("/login"));

    return http.build();
  }
}
