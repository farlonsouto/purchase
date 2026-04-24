package com.example.inventory.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryListener {

  @RabbitListener(queues = "order.created")
  public void handle(String message) {
    System.out.println("Inventory received: " + message);
  }
}
