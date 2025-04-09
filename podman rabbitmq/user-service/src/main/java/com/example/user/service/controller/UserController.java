package com.example.user.service.controller;

import com.example.user.service.dto.LoginDTO;
import com.example.user.service.dto.UserDTO;
import com.example.user.service.model.Users;
import com.example.user.service.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final RabbitTemplate rabbitTemplate;
    private final UserServiceImpl userService;

    public UserController(RabbitTemplate rabbitTemplate, UserServiceImpl userService) {
        this.rabbitTemplate = rabbitTemplate;
        this.userService = userService;
    }

    @GetMapping("/")
    public void redirect(HttpServletResponse response) throws IOException {
        logger.info("Redirecting to Swagger UI");
        response.sendRedirect("/swagger-ui/index.html");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDTO user) {
        String token = UUID.randomUUID().toString();
        logger.info("Registering user with email: {}", user.getEmail());

        userService.registerUser(user, token);

        String confirmationLink = "http://localhost:8082/users/confirmEmail?token=" + token;
        logger.info("Generated confirmation link: {}", confirmationLink);

        rabbitTemplate.convertAndSend("user.exchange", "user.routingKey", user.getEmail());
        logger.info("Sent email to queue for email: {}", user.getEmail());

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO) {
        logger.info("Attempting login for email: {}", loginDTO.getEmail());
        return userService.verify(loginDTO);
    }

    @GetMapping("/get")
    public List<Users> getAllUsers() {
        logger.info("Fetching all users");
        return userService.getAllUsers();
    }

    @GetMapping("/confirmEmail")
    public String confirmEmail(@RequestParam String token) {
        logger.info("Confirming email with token: {}", token);
        return userService.confirmEmail(token);
    }
}

