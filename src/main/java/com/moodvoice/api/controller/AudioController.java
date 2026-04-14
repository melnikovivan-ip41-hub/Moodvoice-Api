package com.moodvoice.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/audio")
@CrossOrigin(origins = "*") // Знову дозволяємо запити з твого GitHub Pages
public class AudioController {

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadAudio(@RequestParam("file") MultipartFile file) {
        
        // Перевіряємо, чи файл не порожній
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "message", "Помилка: Аудіофайл порожній",
                "status", "error"
            ));
        }

        try {
            // Отримуємо інформацію про файл
            String fileName = file.getOriginalFilename();
            long fileSize = file.getSize();
            
            // ТУТ БУДЕ ЛОГІКА АНАЛІЗУ ГОЛОСУ АБО ЗБЕРЕЖЕННЯ В AWS/ХМАРУ
            // Поки що ми просто виводимо розмір файлу в логи Render, щоб довести, що він долетів
            System.out.println("Отримано новий аудіофайл!");
            System.out.println("Назва: " + fileName);
            System.out.println("Розмір: " + (fileSize / 1024) + " KB");

            return ResponseEntity.ok(Map.of(
                "message", "Файл успішно доставлено на сервер! Розмір: " + (fileSize / 1024) + " KB",
                "status", "success"
            ));

        } catch (Exception e) {
            System.err.println("Помилка обробки файлу: " + e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "message", "Помилка сервера при збереженні файлу",
                "status", "error"
            ));
        }
    }
}