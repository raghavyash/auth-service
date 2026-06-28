package com.rsnvtech.erp.edu.controller;

import com.github.cage.Cage;
import com.github.cage.GCage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/captcha")

public class CaptchaController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final Cage cage = new GCage();
    private static final String REDIS_PREFIX = "captcha:";
    private static final long CAPTCHA_EXPIRATION_MINUTES = 5;

    @GetMapping("/generate")
    public ResponseEntity<Map<String, String>> generateCaptcha() throws IOException {
        // 1. Generate a unique key and random text challenge
        String captchaKey = UUID.randomUUID().toString();
        String captchaText = cage.getTokenGenerator().next().substring(0, 5); // 5 character token

        // 2. Save token to Redis with a 5-minute Time-To-Live (TTL)
        redisTemplate.opsForValue().set(
                REDIS_PREFIX + captchaKey,
                captchaText.toLowerCase(),
                CAPTCHA_EXPIRATION_MINUTES,
                TimeUnit.MINUTES
        );

        // 3. Render the text as an image and convert it to a Base64 string
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        cage.draw(captchaText, outputStream);
        String base64Image = Base64.getEncoder().encodeToString(outputStream.toByteArray());

        // 4. Construct response payload
        Map<String, String> response = new HashMap<>();
        response.put("captchaKey", captchaKey);
        response.put("captchaImage", "data:image/png;base64," + base64Image);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyCaptcha(@RequestBody Map<String, String> request) {
        String key = request.get("captchaKey");
        String userInput = request.get("captchaValue");

        Map<String, Object> response = new HashMap<>();

        if (key == null || userInput == null) {
            response.put("success", false);
            response.put("message", "Missing required captcha parameters.");
            return ResponseEntity.badRequest().body(response);
        }

        // Fetch valid token from Redis
        String redisKey = REDIS_PREFIX + key;
        String cachedText = redisTemplate.opsForValue().get(redisKey);

        // Validate and enforce one-time usage (delete token immediately)
        if (cachedText != null && cachedText.equals(userInput.trim().toLowerCase())) {
            redisTemplate.delete(redisKey);
            response.put("success", true);
            response.put("message", "Captcha verified successfully.");
            return ResponseEntity.ok(response);
        } else {
            redisTemplate.delete(redisKey); // Delete even on failure to avoid brute-forcing
            response.put("success", false);
            response.put("message", "Invalid or expired captcha code.");
            return ResponseEntity.status(400).body(response);
        }
    }
}
