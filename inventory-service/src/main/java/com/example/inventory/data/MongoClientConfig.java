package com.example.inventory.data;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoClientConfig {

  private final MongoConfigComponent config;

  public MongoClientConfig(MongoConfigComponent config) {
    this.config = config;
  }

  @Bean
  public MongoClient mongoClient() {
    return MongoClients.create(config.getUri());
  }
}
