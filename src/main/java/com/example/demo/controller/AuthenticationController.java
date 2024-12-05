package com.example.demo.controller;

import java.util.Map;

import com.example.demo.service.kafka.KafkaProducerService;
import com.example.demo.models.*;
import com.example.demo.repositories.UserRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

@Service
public class AuthenticationController {
    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducer;

    @Autowired
    public AuthenticationController(UserRepository userRepository, KafkaProducerService kafkaProducer) {
        this.userRepository = userRepository;
        this.kafkaProducer = kafkaProducer;
    }

    @KafkaListener(topics = "signup", groupId = "apigateway")
    public void SignUp(String message) {
//        System.out.println("\033[33m SignUp: " + message+ "\033[0m");
//        if(userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
//        }
//
//        emailService.setFirstName(signUpRequest.getFirstName());
//        emailService.setLastName(signUpRequest.getLastName());
//        emailService.setEmail(signUpRequest.getEmail());
//        emailService.setPassword(signUpRequest.getPassword());
//
//        emailService.sendConfirmationEmail();
//        return ResponseEntity.ok().body("Success");
    }

    @KafkaListener(topics = "signin", groupId = "apigateway")
    public void signIn(ConsumerRecord<String, String> message) {

        System.out.println("\033[33m key: " + message.key() + "value: " + message.value() + "\033[0m");
        AuthenticationServerResponse response = new AuthenticationServerResponse();

        try {
            SignInRequest signInRequest = new SignInRequest(message.value());
            Optional<User> users = userRepository.findByEmail(signInRequest.getEmail());

            if (users.isPresent() && users.get().getPass().equals(signInRequest.getPassword())) {
                User user = users.get();
                response.setStatus(HttpStatus.OK);
                response.setUser_id(user.getId());
                user.setToken(response.getToken());
                userRepository.save(user);
            } else {
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setMessage("User not found");
            }
        } catch (Exception e){
            System.err.println(e.getMessage());
        } finally {
            System.err.println("response: " + response);
            kafkaProducer.sendMessage("auth_response", message.key() ,response.toJson());
        }
    }


    public void confirmEmail(@RequestBody Map<String, String> token_map) {
//        String token = token_map.get("check_token");
//        System.out.println("\033[34mconfirm(token_map): " + token_map + "\033[0m");
//        System.out.println("\033[34mconfirm(token): " + token + "\033[0m");
//        System.out.println("\033[34mconfirm(token): " + emailService.getToken() + "\033[0m");
//        if(!token.equals(emailService.getToken())) {
//            return ResponseEntity.ok().body("Invalid token");
//        }
//
//        emailService.setToken(null);
//        System.out.println("\033[34mconfirm(status): succses\033[0m");
//
//        try {
//            User user = new User();
//            user.setFirstName(emailService.getFirstName());
//            user.setLastName(emailService.getLastName());
//            user.setEmail(emailService.getEmail());
//            user.setPass(emailService.getPassword());
//
//            Map<String, String> response = generateToken();
//            user.setToken(response.get("token"));
//
//            userRepository.save(user);
//            return ResponseEntity.ok().body(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not found user");
//        }
    }


    @KafkaListener(topics = "logout", groupId = "apigateway")
    public void logout(ConsumerRecord<String, String> message) {
        System.err.println("logout");
        AuthenticationServerResponse response = new AuthenticationServerResponse();

        try {

            Optional<User> users = userRepository.findByToken(message.value());

            if (users.isPresent()) {
                User user = users.get();
                System.err.println("user: " + user);
                user.setToken("");
                response.setStatus(HttpStatus.OK);
                response.setUser_id(user.getId());
                response.setToken("");
                userRepository.save(user);
            } else {
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setMessage("User not found");
            }
        } catch (Exception e){
            System.err.println(e.getMessage());
        } finally {
            System.out.println("\033[33m response: " + response + "\033[0m");
            kafkaProducer.sendMessage("auth_response", message.key() ,response.toJson());
        }
    }
}