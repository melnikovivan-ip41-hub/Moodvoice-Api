package com.moodvoice.api.repository;

import com.moodvoice.api.entity.VoiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VoiceRecordRepository extends JpaRepository<VoiceRecord, Long> {
    // Магия Spring Data: он сам напишет SQL запрос на основе названия этого метода!
    List<VoiceRecord> findByUserEmailOrderByCreatedAtDesc(String userEmail);
}