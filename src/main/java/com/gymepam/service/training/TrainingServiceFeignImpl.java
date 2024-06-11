package com.gymepam.service.training;

import com.gymepam.config.GlobalModelResponse;
import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.domain.dto.records.TrainingRecord.TrainingMicroserviceRequest;
import com.gymepam.domain.dto.records.TrainingRecord.TrainingSummary;
import com.gymepam.service.training.feignClients.TrainingFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TrainingServiceFeignImpl implements TrainingMicroService{

    @Autowired
    private TrainingFeignClient trainingFeignClient;


    @Override
    public ResponseEntity<GlobalModelResponse> saveTraining(TrainingMicroserviceRequest trainingRequest) {
        GlobalModelResponse response = new GlobalModelResponse();
        try{
            trainingFeignClient.saveTraining(trainingRequest);
            response.setResponse("Training Saved");
            return ResponseEntity.ok(response);
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
            trainingFeignClient.updateTrainingStatusToCompleted(trainingId);
            response.setResponse("Training Updated");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error, trying to update Training status to completed in {}", e.getClass().getSimpleName(), e);
            response.setMessage("Error, trying to update Training status to completed");
            return ResponseEntity.badRequest().body(response);
        }
    }
    @Override
    public ResponseEntity<GlobalModelResponse> getTrainingSummaryByTrainerUsername(String trainerUsername){
        GlobalModelResponse response = new GlobalModelResponse();
        try {
            ResponseEntity<TrainingSummary> summary = trainingFeignClient.getTrainingSummaryByTrainerUsername(trainerUsername);
            if (summary.getStatusCode().equals(HttpStatus.OK)) {
                response.setResponse(summary.getBody());
                return ResponseEntity.ok(response);
            }
            log.info("Trainer Summary is empty");

            return ResponseEntity.notFound().build();
        }catch (Exception e) {
            log.error("Error, trying to get Trainer Summary in {}", e.getClass().getSimpleName(), e);
            response.setMessage("Error, trying to get Trainer Summary");
            return ResponseEntity.badRequest().body(response);

        }

    }
    @Override
    public ResponseEntity<GlobalModelResponse> deleteTrainingById(Long trainingId) {
        GlobalModelResponse response = new GlobalModelResponse();
        try{
            HttpStatusCode statusCode = trainingFeignClient.deleteTrainingById(trainingId).getStatusCode();
            if(statusCode.equals(HttpStatus.OK)){
                response.setResponse("Training Deleted");
                log.info("Training Deleted");
                return ResponseEntity.ok(response);
            } else if (statusCode.equals(HttpStatus.NOT_FOUND)) {
                log.info("Training Not Found");
                return ResponseEntity.notFound().build();
            }
            response.setMessage("Training is completed, can't be deleted");
            return ResponseEntity.badRequest().body(response);
        }catch (Exception e) {
            log.error("Error, trying to delete Training {}, in {}",trainingId, e.getClass().getSimpleName(), e);
            response.setMessage("Error, trying to delete Training");
            return ResponseEntity.badRequest().body(response);
        }

    }

    @Override
    public ResponseEntity<GlobalModelResponse> getTrainerTrainingListByTrainingParams(TrainingRecord.TrainerTrainingParamsRequest trainerRequest) {
        GlobalModelResponse response = new GlobalModelResponse();
        try {
            ResponseEntity<List<TrainingRecord.TrainerTrainingResponse>> responseList = trainingFeignClient.getTrainerTrainingListByTrainingParams(trainerRequest);
            if (responseList.getStatusCode().equals(HttpStatus.OK)) {
                response.setResponse(responseList.getBody());
                return ResponseEntity.ok(response);
            }
            log.info("Trainer Training List is empty");
            return ResponseEntity.notFound().build();
        }catch (Exception e) {
            log.error("Error, trying to get Trainer Training List in {}", e.getClass().getSimpleName(), e);
            response.setMessage("Error, trying to get Trainer Training List");
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<GlobalModelResponse> getTraineeTrainingListByTrainingParams(TrainingRecord.TraineeTrainingParamsRequest traineeRequest) {
        GlobalModelResponse response = new GlobalModelResponse();
        try {
            ResponseEntity<List<TrainingRecord.TraineeTrainingResponse>> responseList = trainingFeignClient.getTraineeTrainingListByTrainingParams(traineeRequest);
            if (responseList.getStatusCode().equals(HttpStatus.OK)) {
                response.setResponse(responseList.getBody());
                return ResponseEntity.ok(response);
            }
            log.info("Trainee Training List is empty");
            return ResponseEntity.notFound().build();
        }catch (Exception e) {
            log.error("Error, trying to get Trainee Training List in {}", e.getClass().getSimpleName(), e);
            response.setMessage("Error, trying to get Trainee Training List");
            return ResponseEntity.badRequest().build();
        }
    }

}
