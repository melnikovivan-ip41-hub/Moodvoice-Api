package com.moodvoice.api.controller;

import com.moodvoice.api.entity.User;
import com.moodvoice.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerAnonymous(@RequestBody AuthRequest request) {
        
        // 1. ПЕРЕВІРКА ПОШТИ (Спецсимволи, довжина від 6 до 30 до @, дозволені домени)
        String email = request.getEmail().toLowerCase();
        if (!email.matches("^[a-z0-9._-]{6,30}@(gmail\\.com|ukr\\.net|kpi\\.ua|student\\.kpi\\.ua)$")) {
            return ResponseEntity.badRequest().body(Map.of(
                "message", "Невірний формат пошти. Логін (до @) має бути від 6 до 30 символів. Дозволені домени: @gmail.com, @ukr.net, @kpi.ua", 
                "status", "error"
            ));
        }

        // 2. ПЕРЕВІРКА ПАРОЛЯ НА СПЕЦСИМВОЛИ
        String password = request.getPassword();
        if (!password.matches("^[a-zA-Z0-9]+$")) {
            return ResponseEntity.badRequest().body(Map.of(
                "message", "Пароль не повинен містити спецсимволів (лише англійські літери та цифри).", 
                "status", "error"
            ));
        }

        // 3. Перевіряємо, чи не зайнятий імейл
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of(
                "message", "Email вже зайнятий", 
                "status", "error"
            ));
        }

        // 4. Зберігаємо в базу
        User newUser = new User(request.getEmail(), request.getPassword());
        userRepository.save(newUser);
        
        System.out.println("User saved to DB: " + request.getEmail());
        return ResponseEntity.ok(Map.of(
            "message", "Успішна реєстрація", 
            "status", "success"
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isPresent() && user.get().getPassword().equals(request.getPassword())) {
            System.out.println("Login success: " + request.getEmail());
            return ResponseEntity.ok(Map.of(
                "message", "Вхід успішний", 
                "status", "success"
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of(
                "message", "Невірні дані", 
                "status", "error"
            ));
        }
    }
}

class AuthRequest {
    private String email;
    private String password;
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}