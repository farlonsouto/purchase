package com.example.inventory.data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;

/**
 * Modern best practice: Using a Record with ConfigurationProperties. Prefix 'mongodb' matches the
 * application.properties content (e.g., mongodb.primary.uri).
 */
@ConfigurationProperties(prefix = "mongodb.primary")
@Primary // Marks this as the primary settings bean because multiple DatabaseSettings are present
public record PrimaryMongoSettings(String uri, String database) implements DatabaseSettings {
  @Override
  public String getUri() {
    return uri;
  }

  @Override
  public String getDatabase() {
    return database;
  }
}
