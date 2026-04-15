package com.moodvoice.api.controller;

import com.moodvoice.api.entity.VoiceRecord;
import com.moodvoice.api.repository.VoiceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.List;


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
            String uniqueFileName = UUID.randomUUID().toString() + ".webm";
            String currentDirectory = System.getProperty("user.dir");
            Path filePath = Paths.get(currentDirectory, uniqueFileName);
            
            // 1. Фізичне збереження
            file.transferTo(filePath.toFile());

            // --- 2. ІМІТАЦІЯ РОБОТИ AI (MOCK SERVICE) ---
            String[] transcriptions = {
                "Сьогодні був довгий день, але я відчуваю спокій. Вдалося завершити важливий проєкт на роботі.",
                "Трохи турбуюсь через завтрашню пару, треба ще раз перечитати конспект.",
                "Сьогодні сталося щось чудове! Здав лабораторну роботу на максимальний бал, настрій супер!",
                "Просто звичайний день. Багато рутини, але загалом все стабільно і добре."
            };
            String[] moods = {"Спокій", "Тривога", "Радість", "Баланс"};
            
            // Вибираємо випадковий текст і настрій
            int randomIndex = (int) (Math.random() * transcriptions.length);
            String aiText = transcriptions[randomIndex];
            String aiMood = moods[randomIndex];
            // ----------------------------------------------

            // 3. Збереження в базу (додаємо згенеровані дані ІІ)
            VoiceRecord record = new VoiceRecord(email, filePath.toString(), file.getSize());
            record.setTranscription(aiText);
            record.setMoodType(aiMood);
            voiceRecordRepository.save(record);

            return ResponseEntity.ok(Map.of(
                "message", "Аудіо проаналізовано та збережено!",
                "status", "success",
                "recordId", record.getId().toString()
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage(), "status", "error"));
        }
    }

    @GetMapping("/play/{id}")
    public ResponseEntity<Resource> playAudio(@PathVariable Long id) {
        try {
            // 1. Ищем запись в базе данных по ID
            VoiceRecord record = voiceRecordRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Запись не найдена!"));

            // 2. Берем путь к файлу из базы и находим сам файл
            Path filePath = Paths.get(record.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // 3. Отправляем файл в браузер как аудиопоток
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("audio/webm"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            System.err.println("Ошибка воспроизведения: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getUserHistory(@RequestParam String email) {
        try {
            // Ищем все записи пользователя, отсортированные по дате
            List<VoiceRecord> records = voiceRecordRepository.findByUserEmailOrderByCreatedAtDesc(email);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Ошибка получения истории: " + e.getMessage()));
        }
    }

    // === МЕТОД ДЛЯ УДАЛЕНИЯ ЗАПИСИ ===
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAudio(@PathVariable Long id) {
        try {
            // 1. Находим запись в базе данных
            VoiceRecord record = voiceRecordRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Запис не знайдено"));

            // 2. Находим физический файл на диске и удаляем его
            Path filePath = Paths.get(record.getFilePath());
            Files.deleteIfExists(filePath);

            // 3. Удаляем строку с метаданными из базы (PostgreSQL)
            voiceRecordRepository.delete(record);

            return ResponseEntity.ok(Map.of(
                "status", "success", 
                "message", "Запис успішно видалено"
            ));

        } catch (Exception e) {
            System.err.println("Помилка видалення: " + e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "error", 
                "message", e.getMessage()
            ));
        }
    }
}