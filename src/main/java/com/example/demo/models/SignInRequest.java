package com.example.demo.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {
    private String email;
    private String password;

    public SignInRequest(String massage) {
        try {
            if(massage == null ) throw new RuntimeException("massage == null");
            SignInRequest signInRequest=(new ObjectMapper()).readValue(massage, SignInRequest.class);
            this.email = signInRequest.getEmail();
            this.password = signInRequest.getPassword();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        String json;
        try {
            json = (new ObjectMapper()).writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return json;
    }
}
