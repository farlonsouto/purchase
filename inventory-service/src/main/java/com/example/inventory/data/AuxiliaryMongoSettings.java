package com.example.inventory.data;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Modern best practice: Using a Record with ConfigurationProperties. Prefix 'mongodb.aux' matches
 * the application.properties content (e.g., mongodb.aux.uri).
 */
@Component
@ConfigurationProperties(prefix = "mongodb.aux")
@Qualifier("auxiliary") // Qualifier to distinguish this bean from the primary settings
public record AuxiliaryMongoSettings(String uri, String database) implements DatabaseSettings {
  @Override
  public String getUri() {
    return uri;
  }

  @Override
  public String getDatabase() {
    return database;
  }
}
