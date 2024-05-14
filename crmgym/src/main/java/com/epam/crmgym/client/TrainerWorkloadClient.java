package com.epam.crmgym.client;


import com.epam.crmgym.dto.client.TrainingSessionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "trainer-workload-service")
public interface TrainerWorkloadClient {
    @PostMapping("/workload/sessions")
    String manageTrainingSession(@RequestBody TrainingSessionDTO session);
}

