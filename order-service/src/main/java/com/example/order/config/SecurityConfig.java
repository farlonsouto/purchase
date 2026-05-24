package com.example.order.config;

import com.example.order.security.CustomUserDetailsService;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for the <strong>production</strong> profile.
 *
 * <p>Wires a {@link DaoAuthenticationProvider} backed by {@link CustomUserDetailsService} and
 * {@link BCryptPasswordEncoder} so that credentials are loaded from the database and passwords are
 * compared using BCrypt hashing.
 *
 * <p>HTTP security rules enforced by this configuration:
 *
 * <ul>
 *   <li>Static CSS resources ({@code /css/**}) and the login page ({@code /login}) are publicly
 *       accessible without authentication.
 *   <li>Order-view pages ({@code /view/orders/**}) require the {@code USER} role.
 *   <li>Every other request requires an authenticated session.
 *   <li>Form login uses a custom login page at {@code /login} and redirects to {@code /view/orders}
 *       on successful authentication.
 *   <li>Logout redirects the user back to {@code /login}.
 * </ul>
 *
 * @see CustomUserDetailsService
 * @see DaoAuthenticationProvider
 */
@Configuration
@Profile("prod")
public class SecurityConfig {

  private final com.example.order.security.CustomUserDetailsService userDetailsService;

  /**
   * Constructs a {@code SecurityConfig} with the given {@link CustomUserDetailsService}.
   *
   * @param userDetailsService the service used to load user-specific data during authentication
   */
  public SecurityConfig(CustomUserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  /**
   * Provides a {@link BCryptPasswordEncoder} as the application's {@link PasswordEncoder}.
   *
   * @return a BCrypt-based password encoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Configures a {@link DaoAuthenticationProvider} that delegates user lookup to {@link
   * CustomUserDetailsService} and uses BCrypt for password verification.
   *
   * @return the configured {@link DaoAuthenticationProvider}
   */
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());

    return provider;
  }

  /**
   * Configures the HTTP security filter chain.
   *
   * <ul>
   *   <li>{@code /css/**} and {@code /login} — permit all (no authentication required).
   *   <li>{@code /view/orders/**} — restricted to users with the {@code USER} role.
   *   <li>All other requests — require an authenticated session.
   *   <li>Form login — custom login page at {@code /login}; redirects to {@code /view/orders} after
   *       successful authentication.
   *   <li>Logout — redirects to {@code /login}.
   * </ul>
   *
   * @param http the {@link HttpSecurity} builder
   * @return the built {@link SecurityFilterChain}
   * @throws Exception if the security configuration cannot be applied
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.authenticationProvider(authenticationProvider())
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/css/**", "/login")
                    .permitAll()
                    .requestMatchers("/view/orders/**")
                    .hasRole("USER")
                    .anyRequest()
                    .authenticated())
        .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/view/orders", true))
        .logout(logout -> logout.logoutSuccessUrl("/login"));

    return http.build();
  }
}
