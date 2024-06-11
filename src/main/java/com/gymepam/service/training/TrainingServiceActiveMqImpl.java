package com.gymepam.service.training;

import com.gymepam.config.GlobalModelResponse;
import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.domain.dto.records.TrainingRecord.TrainingMicroserviceRequest;
import com.gymepam.domain.dto.records.TrainingRecord.TrainingSummary;
import com.gymepam.service.messaging.Producer;
import jakarta.validation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class TrainingServiceActiveMqImpl implements TrainingMicroService{

    @Autowired
    private Producer producer;

    @Autowired
    private TrainingInMemoryStorage trainingInMemoryStorage;

    @Override
    public ResponseEntity<GlobalModelResponse> saveTraining(TrainingMicroserviceRequest trainingRequest) {
        GlobalModelResponse response = new GlobalModelResponse();
        try{
            Set<ConstraintViolation<TrainingMicroserviceRequest>> violations;

            try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
                Validator validator = factory.getValidator();
                violations = validator.validate(trainingRequest);
            }

            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
            String processId = producer.sendMessage("queue.saveTraining",trainingRequest);
            log.info("Save training processing (processId):{}",processId);
            String messageResponse = (String) trainingInMemoryStorage.getTrainingResponse(processId);
            if (messageResponse == null) {
                response.setMessage("Save training is processing");
                Map<String, Object> additionalData = new HashMap<>();
                additionalData.put("processId", processId);
                response.setAdditionalData(additionalData);
                return ResponseEntity.ok(response);
            }
            log.info("Training {}", messageResponse);
            if (messageResponse.equals("Saved")) {
                response.setResponse("Training "+ messageResponse);
                return ResponseEntity.ok(response);
            }
            response.setResponse("Training "+ messageResponse);
            return ResponseEntity.badRequest().body(response);
        }catch (ConstraintViolationException cVex){
            log.error("Constraint Violation Exception");
            cVex.getConstraintViolations().forEach(e->log.error(e.getMessage()));
            response.setMessage("Error, trying to save Training, Constraint Violation Exception");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error("Error, trying to save Training in {}", e.getClass().getSimpleName(), e);
            response.setMessage("Error, trying to save Training");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Override
    public ResponseEntity<GlobalModelResponse> updateTrainingStatusToCompleted(Long trainingId) {
        GlobalModelResponse response = new GlobalModelResponse();
        try{
            String processId= producer.sendMessage("queue.updateTraining",trainingId);

            log.info("Update training status processing (processId):{}",processId);
            String  messageResponse = (String) trainingInMemoryStorage.getTrainingResponse(processId);
            if (messageResponse == null) {
                response.setMessage("Update training is processing");
                Map<String, Object> additionalData = new HashMap<>();
                additionalData.put("processId", processId);
                response.setAdditionalData(additionalData);
                return ResponseEntity.ok(response);
            }
            response.setResponse("Training "+ messageResponse);
            log.info("Training {}", messageResponse);
            if (messageResponse.equals("Updated")) {
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error("Error, trying to update Training status to completed in {}", e.getClass().getSimpleName(), e);
            response.setMessage("Error, trying to update Training Status");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Override
    public ResponseEntity<GlobalModelResponse> getTrainingSummaryByTrainerUsername(String trainerUsername){
        GlobalModelResponse response = new GlobalModelResponse();
        try {
            String processId = producer.sendMessage("queue.summaryTrainer",trainerUsername);
            log.info("Trainer summary is processing (processId):{}",processId);
            TrainingSummary  messageResponse = (TrainingSummary) trainingInMemoryStorage.getTrainingResponse(processId);
            if (messageResponse == null) {
                response.setMessage("Trainer summary is processing");
                Map<String, Object> additionalData = new HashMap<>();
                additionalData.put("processId", processId);
                response.setAdditionalData(additionalData);
                return ResponseEntity.ok(response);
            }
            if (!messageResponse.summary().isEmpty()) {
                log.info("Trainer summary returned");
                response.setResponse(messageResponse);
                return ResponseEntity.ok(response);
            }
            log.info("Trainer summary is empty");
            response.setResponse("Trainer summary is empty");
            return ResponseEntity.badRequest().body(response);

        }catch (Exception e) {
            log.error("Error, trying to get Training Summary in {}", e.getClass().getSimpleName(), e);
            response.setMessage("Error, trying to get Training Summary");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Override
    public ResponseEntity<GlobalModelResponse> deleteTrainingById(Long trainingId) {
        GlobalModelResponse response = new GlobalModelResponse();
        try {
            String processId = producer.sendMessage("queue.deleteTraining",trainingId);

            log.info("Delete Training {} is processing",trainingId);
            String messageResponse = (String) trainingInMemoryStorage.getTrainingResponse(processId);
            if (messageResponse == null) {
                response.setMessage("Delete Training is processing");
                Map<String, Object> additionalData = new HashMap<>();
                additionalData.put("processId", processId);
                response.setAdditionalData(additionalData);
                return ResponseEntity.ok(response);
            }
            response.setResponse("Training "+ messageResponse);
            log.info("Training {}", messageResponse);
            if (messageResponse.equals("Deleted")) {
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body(response);
        }catch (Exception e) {
            log.error("Error, trying to delete Training {}, in {}",trainingId, e.getClass().getSimpleName(), e);
            response.setMessage("Error, trying to delete Training "+trainingId);
            return ResponseEntity.badRequest().body(response);
        }

    }

    @Override
    public ResponseEntity<GlobalModelResponse> getTrainerTrainingListByTrainingParams(TrainingRecord.TrainerTrainingParamsRequest trainerRequest) {
        GlobalModelResponse response = new GlobalModelResponse();
        try {
            String processId = producer.sendMessage("queue.trainerTrainingList",trainerRequest);
            log.info("Trainer training list processing");
            List<TrainingRecord.TrainerTrainingResponse> messageResponse = (List<TrainingRecord.TrainerTrainingResponse>) trainingInMemoryStorage.getTrainingResponse(processId);
            if (messageResponse == null) {
                response.setMessage("Get Trainer Training List is processing");
                Map<String, Object> additionalData = new HashMap<>();
                additionalData.put("processId", processId);
                response.setAdditionalData(additionalData);
                return ResponseEntity.ok(response);
            }
            if (!messageResponse.isEmpty()) {
                response.setResponse(messageResponse);
                return ResponseEntity.ok().body(response);
            }
            response.setMessage("Get Trainer Training List is empty");
            return ResponseEntity.badRequest().body(response);
        }catch (Exception e) {
            log.error("Error, trying to get Trainer training list {}, in {}",trainerRequest, e.getClass().getSimpleName(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<GlobalModelResponse> getTraineeTrainingListByTrainingParams(TrainingRecord.TraineeTrainingParamsRequest traineeRequest) {
        GlobalModelResponse response = new GlobalModelResponse();
        try {
            String processId = producer.sendMessage("queue.traineeTrainingList",traineeRequest);
            log.info("Trainee training list processing");
            Thread.sleep(2000);
            List<TrainingRecord.TraineeTrainingResponse> messageResponse = (List<TrainingRecord.TraineeTrainingResponse>) trainingInMemoryStorage.getTrainingResponse(processId);
            if (messageResponse == null) {
                response.setMessage("Get Trainee Training List is processing");
                Map<String, Object> additionalData = new HashMap<>();
                additionalData.put("processId", processId);
                response.setAdditionalData(additionalData);
                return ResponseEntity.ok(response);
            }
            if (!messageResponse.isEmpty()) {
                response.setResponse(messageResponse);
                return ResponseEntity.ok().body(response);
            }
            response.setMessage("Get Trainee Training List is empty");
            return ResponseEntity.badRequest().body(response);
        }catch (Exception e) {
            log.error("Error, trying to get Trainee training list {}, in {}",traineeRequest, e.getClass().getSimpleName(), e);
            return ResponseEntity.badRequest().build();
        }
    }

}
