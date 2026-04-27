package com.example.inventory.data;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/** This class is a Spring Configuration because it is creating Beans */
@Configuration
public class MongoClientConfig {

  @Bean // This is a Bean because I can't write @Component over MongoClient, it's 3rd-party
  @Primary // The default client if none is specified
  public MongoClient primaryMongoClient(DatabaseSettings settings) {
    return MongoClients.create(settings.getUri());
  }

  @Bean
  @Qualifier("auxiliary") // Qualifier to distinguish this client from the primary one
  public MongoClient secondaryMongoClient(@Qualifier("auxiliary") DatabaseSettings settings) {
    return MongoClients.create(settings.getUri());
  }
}
