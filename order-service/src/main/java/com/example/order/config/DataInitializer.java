package com.example.order.config;

import com.example.order.model.AppUser;
import com.example.order.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("prod")
public class DataInitializer {

  @Bean
  CommandLineRunner initUsers(UserRepository repository, PasswordEncoder encoder) {

    return args -> {
      if (repository.findByUsername("user").isEmpty()) {
        repository.save(new AppUser("user", encoder.encode("password"), "USER"));
      }
    };
  }
}
