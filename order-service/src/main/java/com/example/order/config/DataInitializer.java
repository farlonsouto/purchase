package com.example.order.config;

import com.example.order.model.AppUser;
import com.example.order.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Seeds the database with initial user data on application startup.
 *
 * <h2>Why this class exists</h2>
 *
 * <p>In the production profile ({@code @Profile("prod")}), authentication is backed by a real
 * database via {@code CustomUserDetailsService}. Unlike the dev/test profile — which uses an
 * in-memory {@code InMemoryUserDetailsManager} pre-populated at bean creation time — the
 * production database starts empty. Without a seed record, no one could log in on the very
 * first boot.
 *
 * <h2>How it fits into Spring Boot's lifecycle</h2>
 *
 * <p>{@link CommandLineRunner} is a Spring Boot hook that runs <em>after the application
 * context is fully initialised</em> but <em>before the application starts serving requests</em>.
 * That timing is important: by the time {@code initUsers} executes, the JPA schema has already
 * been created/validated by Hibernate, and all beans (including the {@link PasswordEncoder} and
 * the {@link UserRepository}) are ready to use.
 *
 * <h2>Idempotency</h2>
 *
 * <p>The {@code if (repository.findByUsername(...).isEmpty())} guard makes the initialiser
 * <em>idempotent</em>: subsequent restarts will not create duplicate rows or throw unique-key
 * violations — safe to run in any environment.
 *
 * <h2>Security note</h2>
 *
 * <p>The password is stored as a BCrypt hash (via the injected {@link PasswordEncoder}),
 * never in plain text. In a real production system you would externalise the initial
 * credentials (e.g. via environment variables or a secrets manager) instead of hard-coding
 * them here.
 *
 * @see CommandLineRunner
 * @see com.example.order.security.CustomUserDetailsService
 */
@Configuration
@Profile("prod")
public class DataInitializer {

  /**
   * Returns a {@link CommandLineRunner} that inserts a default {@code user} account into the
   * database if one does not already exist.
   *
   * <p>Spring Boot automatically detects every {@link CommandLineRunner} bean in the context
   * and calls its {@code run} method once, in order, right after startup.
   *
   * @param repository the JPA repository used to check for and persist the user
   * @param encoder    the password encoder used to hash the plain-text password before storage
   * @return a {@link CommandLineRunner} that performs the conditional seed operation
   */
  @Bean
  CommandLineRunner initUsers(UserRepository repository, PasswordEncoder encoder) {

    return args -> {
      if (repository.findByUsername("user").isEmpty()) {
        repository.save(new AppUser("user", encoder.encode("password"), "USER"));
      }
    };
  }
}
