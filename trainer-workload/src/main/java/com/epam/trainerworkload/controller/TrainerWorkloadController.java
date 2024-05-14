package com.epam.trainerworkload.controller;


import com.epam.trainerworkload.dto.TrainingSessionDTO;
import com.epam.trainerworkload.entity.TrainingSession;
import com.epam.trainerworkload.service.TrainerWorkloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workload")
public class TrainerWorkloadController {

    private final TrainerWorkloadService service;

    @Autowired
    public TrainerWorkloadController(TrainerWorkloadService service) {
        this.service = service;
    }

    @PostMapping("/sessions")
    public ResponseEntity<String> addTrainingSession(@RequestBody TrainingSessionDTO dto) {
        service.addOrUpdateTrainingSession(dto);
        return ResponseEntity.ok("Session added successfully");
    }

    @GetMapping("/sessions/{username}/{year}/{month}")
    public ResponseEntity<List<TrainingSession>> getTrainingSessions(@PathVariable String username, @PathVariable int year, @PathVariable int month) {
        List<TrainingSession> sessions = service.getTrainerMonthlyWorkload(username, year, month);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/total-hours/{username}/{year}/{month}")
    public ResponseEntity<Integer> getTotalTrainingHours(@PathVariable String username, @PathVariable int year, @PathVariable int month) {
        int totalHours = service.getTotalTrainingHours(username, year, month);
        return ResponseEntity.ok(totalHours);
    }
}

