package com.epam.trainerworkload.service;

import com.epam.trainerworkload.dto.TrainingSessionDTO;
import com.epam.trainerworkload.entity.TrainingSession;
import com.epam.trainerworkload.repository.TrainingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerWorkloadService {


    private final TrainingSessionRepository repository;

    @Autowired
    public TrainerWorkloadService(TrainingSessionRepository repository) {
        this.repository = repository;
    }

    public void addOrUpdateTrainingSession(TrainingSessionDTO dto) {
        TrainingSession session = new TrainingSession();
        session.setTrainerUsername(dto.getTrainerUsername());
        session.setFirstName(dto.getFirstName());
        session.setLastName(dto.getLastName());
        session.setIsActive(dto.isActive());
        session.setTrainingDate(dto.getTrainingDate());
        session.setTrainingDuration(dto.getTrainingDuration());
        repository.save(session);
    }

    public List<TrainingSession> getTrainerMonthlyWorkload(String username, int year, int month) {
        // Here would implement logic to filter by username, year, and month
        return repository.findAll();
    }

    public Integer getTotalTrainingHours(String username, int year, int month) {
        List<TrainingSession> sessions = repository.findTrainingSessionsByTrainerAndMonth(username, year, month);
        return sessions.stream()
                .mapToInt(TrainingSession::getTrainingDuration)
                .sum();
    }
}

