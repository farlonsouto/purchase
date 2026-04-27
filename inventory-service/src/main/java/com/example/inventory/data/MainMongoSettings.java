package com.example.inventory.data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Modern best practice: Using a Record with ConfigurationProperties. Prefix 'mongodb' matches the
 * application.properties content (e.g., mongodb.main.uri).
 */
@ConfigurationProperties(prefix = "mongodb.main")
@Primary // Marks this as the primary settings bean because multiple DatabaseSettings are present
public record MainMongoSettings(String uri, String database) implements DatabaseSettings {
  @Override
  public String getUri() {
    return uri;
  }

  @Override
  public String getDatabase() {
    return database;
  }
}
