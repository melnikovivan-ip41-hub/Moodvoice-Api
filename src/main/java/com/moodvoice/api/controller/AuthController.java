package com.moodvoice.api.controller;

import com.moodvoice.api.entity.User;
import com.moodvoice.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository; // Автоматичне підключення бази

    @PostMapping("/register")
    public ResponseEntity<String> registerAnonymous(@RequestBody AuthRequest request) {
        // Перевіряємо, чи не зайнятий імейл
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Email вже зайнятий\", \"status\": \"error\"}");
        }

        // Зберігаємо в базу
        User newUser = new User(request.getEmail(), request.getPassword());
        userRepository.save(newUser);
        
        System.out.println("User saved to DB: " + request.getEmail());
        return ResponseEntity.ok("{\"message\": \"Успішна реєстрація\", \"status\": \"success\"}");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isPresent() && user.get().getPassword().equals(request.getPassword())) {
            System.out.println("Login success: " + request.getEmail());
            return ResponseEntity.ok("{\"message\": \"Вхід успішний\", \"status\": \"success\"}");
        } else {
            return ResponseEntity.status(401).body("{\"message\": \"Невірні дані\", \"status\": \"error\"}");
        }
    }
}

// Клас AuthRequest залишається тут же внизу, як у тебе було
class AuthRequest {
    private String email;
    private String password;
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
