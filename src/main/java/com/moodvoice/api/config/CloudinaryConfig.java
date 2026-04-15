package com.moodvoice.api.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        // СКОПИРУЙ ЭТИ ТРИ ЗНАЧЕНИЯ ИЗ СВОЕГО КАБИНЕТА CLOUDINARY:
        config.put("cloud_name", "dpvr9y8d7");
        config.put("api_key", "248946228945837");
        config.put("api_secret", "hKdVJ7p8iY7UtQvudsmso00Ha7U");
        
        return new Cloudinary(config);
    }
}