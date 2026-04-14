package com.moodvoice.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "records")
public class VoiceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Почта пользователя, чтобы знать, чей это запесь
    @Column(nullable = false)
    private String userEmail;

    // Путь к файлу на сервере (например: "/uploads/abc-123.webm")
    @Column(nullable = false)
    private String filePath;

    // Тип настроения (будет приходить от AI: "Радость", "Спокойствие" и т.д.)
    private String moodType;

    // Текстовая расшифровка (транскрипция) голоса
    @Column(columnDefinition = "TEXT")
    private String transcription;

    // Дата создания записи
    private LocalDateTime createdAt;

    // Размер файла в байтах
    private Long fileSize;

    // Пустой конструктор (обязателен для JPA)
    public VoiceRecord() {}

    // Конструктор для удобства
    public VoiceRecord(String userEmail, String filePath, Long fileSize) {
        this.userEmail = userEmail;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.createdAt = LocalDateTime.now();
    }

    // Геттеры и сеттеры (обязательно добавь их для всех полей)
    public Long getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getMoodType() { return moodType; }
    public void setMoodType(String moodType) { this.moodType = moodType; }
    public String getTranscription() { return transcription; }
    public void setTranscription(String transcription) { this.transcription = transcription; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getFileSize() { return fileSize; }
}