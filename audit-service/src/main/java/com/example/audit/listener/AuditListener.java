package com.example.audit.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AuditListener {

  @RabbitListener(queues = "order.created")
  public void audit(String event) {
    System.out.println("Audit log: " + event);
  }
}
