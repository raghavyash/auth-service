package com.rsnvtech.erp.edu.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    @GetMapping("/search")
    public ResponseEntity<Boolean> getStudentList(
            @RequestParam Long model) {
        //List<StudentNotificationModel> studentModel = studentService.getStudentDetailList(model);
        return ResponseEntity.ok(null);
    }

}
