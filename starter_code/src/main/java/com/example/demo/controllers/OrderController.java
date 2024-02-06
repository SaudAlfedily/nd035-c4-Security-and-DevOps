package com.example.demo.controllers;

import com.example.demo.model.User;
import com.example.demo.model.UserOrder;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {



    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderRepository orderRepository;


    @PostMapping("/submit/{username}")
    public ResponseEntity<UserOrder> submit(@PathVariable String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {

			logger.error("order not created{}",username);
            return ResponseEntity.notFound().build();
        }

        UserOrder order = UserOrder.createFromCart(user.getCart());
        orderRepository.save(order);
        logger.info("order created for{}",username);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/history/{username}")
    public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {

			logger.error("no orders available for{}",username);
            return ResponseEntity.notFound().build();

		}

        return ResponseEntity.ok(orderRepository.findByUser(user));
    }
}
