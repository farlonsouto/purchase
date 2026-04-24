package com.example.order.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RestController
@RequestMapping("/orders")
public class OrderController {

  private final RabbitTemplate rabbitTemplate;

  public OrderController(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @PostMapping
  public String createOrder(@RequestBody String order) {
    rabbitTemplate.convertAndSend("order.exchange", "order.created", order);
    return "Order created: " + order;
  }
}
