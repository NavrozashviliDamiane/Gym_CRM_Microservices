package com.epam.crmgym.service.impl;

import com.epam.crmgym.client.TrainerWorkloadClient;
import com.epam.crmgym.dto.client.TrainingSessionDTO;
import lombok.extern.slf4j.Slf4j;
import com.epam.crmgym.entity.*;
import com.epam.crmgym.repository.TraineeRepository;
import com.epam.crmgym.repository.TrainerRepository;
import com.epam.crmgym.repository.TrainingRepository;
import com.epam.crmgym.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    private final TrainerWorkloadClient trainerWorkloadClient;


    @Autowired
    public TrainingServiceImpl(TrainingRepository trainingRepository, TraineeRepository traineeRepository, TrainerRepository trainerRepository, TrainerWorkloadClient trainerWorkloadClient) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainerWorkloadClient = trainerWorkloadClient;
    }



    @Override
    @Transactional
    public Training createTraining(String username, String trainerUsername,
                                   String trainingName, Date trainingDate, Integer trainingDuration){


        Trainee trainee = traineeRepository.findByUserUsername(username);

        Trainer trainer = trainerRepository.findByUserUsername(trainerUsername);

        TrainingType trainingType = trainer.getTrainingType();



        Training training = new Training();

        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(trainingName);
        training.setTrainingDate(trainingDate);
        training.setTrainingDuration(trainingDuration);
        training.setTrainingType(trainingType);

        TrainingSessionDTO sessionDTO = new TrainingSessionDTO(trainerUsername, trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(), trainer.getUser().isActive(),
                trainingDate, trainingDuration, "ADD");
        trainerWorkloadClient.manageTrainingSession(sessionDTO);


        log.info("Training Created Successfully");
        return trainingRepository.save(training);
    }



}
