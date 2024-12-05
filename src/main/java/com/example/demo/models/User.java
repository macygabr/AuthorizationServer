package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users_auth")
public class User {
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String pass;

    @Column
    private String token;

}
