package com.epam.crmgym.service.impl;

import lombok.extern.slf4j.Slf4j;
import com.epam.crmgym.entity.*;
import com.epam.crmgym.repository.TraineeRepository;
import com.epam.crmgym.repository.TrainerRepository;
import com.epam.crmgym.repository.TrainingRepository;
import com.epam.crmgym.repository.TrainingTypeRepository;
import com.epam.crmgym.service.AuthenticateService;
import com.epam.crmgym.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final AuthenticateService authenticateService;

    @Autowired
    public TrainingServiceImpl(TrainingRepository trainingRepository, TraineeRepository traineeRepository, TrainerRepository trainerRepository, TrainingTypeRepository trainingTypeRepository, AuthenticateService authenticateService) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.authenticateService = authenticateService;
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


        log.info("Training Created Successfully");
        return trainingRepository.save(training);
    }



}
