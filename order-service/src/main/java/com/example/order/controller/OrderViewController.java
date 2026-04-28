package com.example.order.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for UI rendering via Template Engines (e.g., Thymeleaf).
 *
 * <p>Unlike a {@code @RestController}, which serializes data directly into HTTP response bodies
 * (JSON/XML), this class returns logical View names (Strings). These names are intercepted by the
 * ViewResolver to locate and render physical templates.
 */
@Controller
@RequestMapping("/view/orders")
public class OrderViewController {

  /**
   * The gateway to RabbitMQ infrastructure. Used here to implement a "Fire and Forget" pattern,
   * decoupling the UI request from the potentially heavy downstream order processing.
   */
  private final RabbitTemplate rabbitTemplate;

  /**
   * In-memory state storage. NOTE: Spring Beans are Singletons by default. Since this list is an
   * instance member, it is shared across all concurrent web requests. It is not thread-safe in its
   * current form.
   */
  private final List<String> orders = new ArrayList<>();

  /**
   * Constructor-based dependency injection. This is the preferred method over field injection
   * (@Autowired) as it enforces immutability (final fields) and ensures the controller cannot be
   * instantiated without its required dependencies, facilitating cleaner unit tests.
   */
  public OrderViewController(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @GetMapping("/")
  public String home() {
    return "redirect:/view/orders";
  }

  /**
   * Handles the submission of a new order from a web form.
   *
   * @param product The data bound from the request parameters.
   * @return A redirection instruction. Using "redirect:" triggers the Post/Redirect/Get (PRG)
   *     pattern, which prevents duplicate form submissions if the user refreshes their browser.
   */
  @PostMapping
  public String createOrder(@RequestParam String product) {
    // Dispatches the message to the broker. The exchange routes it based on the routing key.
    // Logic note: The product is sent to the queue but NOT added to the local 'orders' list here.
    rabbitTemplate.convertAndSend("order.exchange", "order.created", product);
    return "redirect:/view/orders";
  }

  /**
   * Prepares the UI model and resolves the display template.
   *
   * @param model A container for data that needs to be accessible by the View layer.
   * @return The logical name of the template (e.g., maps to
   *     src/main/resources/templates/login.html).
   */
  @GetMapping
  public String getOrders(Model model) {
    // Exposes the current state of the orders list to the UI rendering engine.
    model.addAttribute("orders", orders);
    return "redirect:/view/orders";
  }
}
