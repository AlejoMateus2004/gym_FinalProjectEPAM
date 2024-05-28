package com.gymepam.service.messaging;

import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.service.training.TrainingInMemoryStorage;
import jakarta.jms.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class Listener {
    @Autowired
    private MessageConverter messageConverter;
    @Autowired
    private TrainingInMemoryStorage trainingInMemoryStorage;

    @JmsListener(destination = "queue.saveTraining.response", containerFactory = "jmsListenerContainerFactory")
    public void getResponseSaveTraining(Message message) {
        try {
            String processId = message.getStringProperty("processId");
            if (processId == null || processId.isEmpty()) {
                log.info("No response");
                return;
            }
            String response = messageConverter.convertMessageToObject(message, String.class);
            if (response == null) {
                log.info("No response");
                return;
            }
            trainingInMemoryStorage.setTrainingResponse(processId, response);
            log.info("{} Training {}",response, processId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    @JmsListener(destination = "queue.updateTraining.response")
    public void getResponseUpdateTrainingStatus(Message message){
        try {
            String processId = message.getStringProperty("processId");
            if (processId == null || processId.isEmpty()) {
                log.info("No response");
                return;
            }
            String response = messageConverter.convertMessageToObject(message, String.class);
            if (response == null) {
                log.info("No response");
                return;
            }
            trainingInMemoryStorage.setTrainingResponse(processId, response);
            log.info("{} Training {}",response, processId);

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    @JmsListener(destination = "queue.summaryTrainer.response")
    public void getResponseTrainingSummary(Message message){
        try {
            String processId = message.getStringProperty("processId");
            if (processId == null || processId.isEmpty()) {
                log.info("No response");
                return;
            }
            TrainingRecord.TrainingSummary trainingSummary  = messageConverter.convertMessageToObject(message, TrainingRecord.TrainingSummary.class);
            if (trainingSummary == null || trainingSummary.summary().isEmpty()) {
                log.info("No response");
                return;
            }
            trainingInMemoryStorage.setTrainingResponse(processId, trainingSummary);
            log.info("Trainer summary obtained (processId): {}", processId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }


    }

    @JmsListener(destination = "queue.trainerTrainingList.response")
    public void getResponseTrainerTrainingList(Message message){
        try {
            String processId = message.getStringProperty("processId");
            if (processId == null || processId.isEmpty()) {
                log.info("No response");
                return;
            }
            List<TrainingRecord.TrainerTrainingResponse> response  = messageConverter.convertMessageToObject(message, List.class);
            if (response == null) {
                log.info("No response");
                return;
            }
            trainingInMemoryStorage.setTrainingResponse(processId, response);
            log.info("Trainer Training List obtained (processId): {}", processId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    @JmsListener(destination = "queue.traineeTrainingList.response")
    public void getTraineeTrainingListByTrainingParams(Message message){
        try {
            String processId = message.getStringProperty("processId");
            if (processId == null || processId.isEmpty()) {
                log.info("No response");
                return;
            }
            List<TrainingRecord.TrainerTrainingResponse> response  = messageConverter.convertMessageToObject(message, List.class);

            if (response == null || response.isEmpty()) {
                log.info("No response");
                return;
            }
            trainingInMemoryStorage.setTrainingResponse(processId, response);
            log.info("Trainee Training List (processId): {}", processId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    @JmsListener(destination = "queue.deleteTraining.response")
    public void deleteTrainingById(Message message){
        try {
            String processId = message.getStringProperty("processId");
            if (processId == null || processId.isEmpty()) {
                log.info("No response");
                return;
            }
            String response  = messageConverter.convertMessageToObject(message, String.class);
            if (response == null) {
                log.info("No response");
                return;
            }
            trainingInMemoryStorage.setTrainingResponse(processId, response);
            log.info("{} Training {}",response, processId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
