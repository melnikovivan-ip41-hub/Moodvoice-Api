package com.moodvoice.api.controller;

import com.moodvoice.api.entity.VoiceRecord;
import com.moodvoice.api.repository.VoiceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/audio")
@CrossOrigin(origins = "*")
public class AudioController {

    @Autowired
    private VoiceRecordRepository voiceRecordRepository;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadAudio(
            @RequestParam("file") MultipartFile file,
            @RequestParam("email") String email) { // Добавили email, чтобы знать чей файл

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Файл пустой", "status", "error"));
        }

        try {
            // 1. Генерируем уникальное имя файла, чтобы они не перезаписывали друг друга
            String uniqueFileName = UUID.randomUUID().toString() + ".webm";
            
            // 2. Определяем путь сохранения (в папку проекта)
            String currentDirectory = System.getProperty("user.dir");
            Path filePath = Paths.get(currentDirectory, uniqueFileName);
            
            // 3. ФИЗИЧЕСКОЕ СОХРАНЕНИЕ НА ДИСК
            file.transferTo(filePath.toFile());

            // 4. СОХРАНЕНИЕ В БАЗУ ДАННЫХ (используем твой новый класс)
            VoiceRecord record = new VoiceRecord(email, filePath.toString(), file.getSize());
            voiceRecordRepository.save(record);

            return ResponseEntity.ok(Map.of(
                "message", "Запись сохранена в базу и на диск!",
                "status", "success",
                "recordId", record.getId().toString()
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage(), "status", "error"));
        }
    }
}