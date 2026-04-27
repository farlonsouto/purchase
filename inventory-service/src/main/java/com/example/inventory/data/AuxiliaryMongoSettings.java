package com.example.inventory.data;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Modern best practice: Using a Record with ConfigurationProperties. Prefix 'mongodb' matches the
 * application.properties content (e.g., mongodb.primary.uri).
 */
@ConfigurationProperties(prefix = "mongodb.primary")
public record SecondaryMongoSettings(String uri, String database) implements DatabaseSettings {
  @Override
  public String getUri() {
    return uri;
  }

  @Override
  public String getDatabase() {
    return database;
  }
}
