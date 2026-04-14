package com.moodvoice.api.repository;

import com.moodvoice.api.entity.VoiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VoiceRecordRepository extends JpaRepository<VoiceRecord, Long> {
    // Этот метод позволит нам найти все записи конкретного пользователя для архива
    List<VoiceRecord> findByUserEmail(String userEmail);
}
