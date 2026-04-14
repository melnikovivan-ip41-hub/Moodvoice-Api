package com.moodvoice.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // Так буде називатися таблиця в PostgreSQL
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    // Конструктори, геттери та сеттери
    public User() {}
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}