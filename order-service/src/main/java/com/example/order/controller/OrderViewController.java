package com.example.order.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderViewController {

  private final List<String> orders = new ArrayList<>();

  @GetMapping("/view/orders")
  public String getOrders(Model model) {
    model.addAttribute("orders", orders);
    return "orders";
  }

  @PostMapping("/view/orders")
  public String createOrder(@RequestParam String product) {
    orders.add(product);
    return "redirect:/orders";
  }
}
