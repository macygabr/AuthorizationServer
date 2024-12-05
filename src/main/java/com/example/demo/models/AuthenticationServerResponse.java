package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationServerResponse {
    @JsonProperty("user_id")
    private Long user_id = 0L;

    @JsonProperty("token")
    private String token = UUID.randomUUID().toString();
    @JsonProperty("token_name")
    private String tokenName = "authToken";

    @JsonProperty("status")
    private HttpStatus status;
    @JsonProperty("message")
    private String message = "";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public AuthenticationServerResponse(String message) {
        try {
            if (message == null) throw new RuntimeException("Message is null");
            AuthenticationServerResponse response = new ObjectMapper().readValue(message, AuthenticationServerResponse.class);
            this.user_id = response.getUser_id();
            this.token = response.getToken();
            this.tokenName = response.getTokenName();
            this.status = response.getStatus();
            this.message = response.getMessage();
        } catch (Exception e) {
            System.err.println("Failed to parse message: " + e.getMessage());
        }
    }

    public String toJson(){
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing UserRequest", e);
        }
    }
}
