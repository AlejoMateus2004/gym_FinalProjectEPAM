package com.gymepam.service.storage;

import com.gymepam.config.AppProperties;
import com.gymepam.config.ElasticSearchProperties;
import com.gymepam.domain.*;
import com.gymepam.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
@Log4j2
public class InitializeStorageService {

    private static final Map<String, Map<Long, Long>> IDS_MAP = new HashMap<>();

    @Autowired private final ElasticSearchProperties elasticSearchProperties;
    @Autowired private final AppProperties appProperties;

    @Autowired private final StorageService storageService;
    @Autowired private final TraineeService traineeService;
    @Autowired private final TrainerService trainerService;
    @Autowired private final UserService userService;
    @Autowired private final TrainingService trainingService;
    @Autowired private final TrainingTypeService trainingTypeService;

//    @PostConstruct
    public void initialize() {
        try (Workbook workbook = WorkbookFactory.create(new FileInputStream( appProperties.getExcelFilePath() ))) {
            loadDataFromExcel(workbook);
            log.info("Data loaded from file");
        } catch (Exception e) {
            log.error("Error loading data from file", e);
        }
    }

    public void loadDataFromExcel(Workbook workbook) {
        try {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();

                if ("Trainee".equals(sheetName)) {
                    processTraineeSheet(sheet);
                } else if ("Training_Type".equals(sheetName)) {
                    processTrainingTypeSheet(sheet);
                } else if ("Trainer".equals(sheetName)) {
                    processTrainerSheet(sheet);
                } else if ("Training".equals(sheetName)) {
                    processTrainingSheet(sheet);
                } else {
                    log.warn("Object unknown: {}", sheetName);
                }
            }
            log.info("File has been uploaded successfully");
        } catch (Exception e) {
            log.error("Error while File is uploaded", e);
        }
    }
    // Process Trainee sheet
    private void processTraineeSheet(Sheet sheet) {
        Map<Long, Long> idMap = new HashMap<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            Map<String, Object> traineeData = extractDataFromRow(sheet.getRow(0), row);
            if (traineeData == null) {
                continue;
            }
            Long idFieldFileTrainee = (Long) traineeData.get("Id");


            Trainee trainee = getDataTrainee(traineeData);
            if (trainee != null) {
                Trainee result = traineeService.saveTrainee(trainee);

                if (appProperties.getActiveProfile().equals("inmemory")) {
                    userService.saveUser(result.getUser());
                }
                idMap.put(idFieldFileTrainee, result.getId());
            }
        }
        IDS_MAP.put("Trainee", idMap);
    }

    // Process Training_Type sheet
    private void processTrainingTypeSheet(Sheet sheet) {
        Map<Long, Long> idMap = new HashMap<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            Map<String, Object> trainingTypeData = extractDataFromRow(sheet.getRow(0), row);
            if (trainingTypeData == null) {
                continue;
            }
            Long idFieldFileTrainingType = (Long) trainingTypeData.get("trainingTypeId");

            TrainingType trainingType = getDataTrainingType(trainingTypeData);

            if (trainingType != null) {
                TrainingType result =  trainingTypeService.saveTraining_Type(trainingType);
                idMap.put(idFieldFileTrainingType, result.getId());
            }
        }
        IDS_MAP.put("TrainingType", idMap);
    }

    // Process Trainer sheet
    private void processTrainerSheet(Sheet sheet) {
        Map<Long, Long> idMap = new HashMap<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            Map<String, Object> trainerData = extractDataFromRow(sheet.getRow(0), row);
            if (trainerData == null) {
                continue;
            }
            Long idFieldFileTrainer = (Long) trainerData.get("Id");

            trainerData.put("trainingTypeId", IDS_MAP.get("TrainingType").get(trainerData.get("trainingTypeId")));


            Trainer trainer = getDataTrainer(trainerData);
            if (trainer != null) {
                Trainer result =trainerService.saveTrainer(trainer);
                if (appProperties.getActiveProfile().equals("inmemory")) {
                    userService.saveUser(result.getUser());
                }
                idMap.put(idFieldFileTrainer, result.getId());
            }
        }
        IDS_MAP.put("Trainer", idMap);
    }

    // Process Training sheet
    private void processTrainingSheet(Sheet sheet) {
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            Map<String, Object> trainingData = extractDataFromRow(sheet.getRow(0), row);
            if (trainingData == null) {
                continue;
            }
            trainingData.put("TraineeId", IDS_MAP.get("Trainee").get(trainingData.get("TraineeId")));
            trainingData.put("TrainerId", IDS_MAP.get("Trainer").get(trainingData.get("TrainerId")));
            trainingData.put("trainingTypeId", IDS_MAP.get("TrainingType").get(trainingData.get("trainingTypeId")));

            Training training = getDataTraining(trainingData);
            if (training != null) {
                trainingService.saveTraining(training);
            }
        }
    }

    private Map<String, Object> extractDataFromRow(Row headerRow, Row dataRow) {
        Map<String, Object> data = new HashMap<>();

        for (int j = 0; j < headerRow.getLastCellNum(); j++) {
            Cell dataCell = dataRow.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String header = getCellValue(headerRow.getCell(j)).toString();
            Object cellValue = getCellValue(dataCell);
            if (cellValue == null || cellValue == "") {
                return null;
            }
            data.put(header, cellValue);
        }
        return data;
    }

    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate();
                } else {
                    return (long) cell.getNumericCellValue();
                }
            default:
                return null;
        }
    }

    private Trainee getDataTrainee(Map<String, Object> traineeData) {
        Trainee trainee = new Trainee();
        trainee.setId((Long) traineeData.get("Id"));
        trainee.setAddress((String) traineeData.get("address"));
        trainee.setDateOfBirth((LocalDate) traineeData.get("dateOfBirth"));
        trainee.setUser(getUser(traineeData));
        return (trainee.getUser() != null) ? trainee : null;
    }

    private Trainer getDataTrainer(Map<String, Object> trainerData) {
        Trainer trainer = new Trainer();
        trainer.setId((Long) trainerData.get("Id"));
        trainer.setTrainingType(trainingTypeService.getTraining_Type((Long) trainerData.get("trainingTypeId")));
        trainer.setUser(getUser(trainerData));
        return (trainer.getUser() != null) ? trainer : null;
    }

    private TrainingType getDataTrainingType(Map<String, Object> trainingTypeData) {
        TrainingType trainingType = new TrainingType();
        trainingType.setId((Long) trainingTypeData.get("trainingTypeId"));
        trainingType.setTrainingTypeName((String) trainingTypeData.get("trainingTypeName"));
        return trainingType;
    }

    private Training getDataTraining(Map<String, Object> trainingData) {
        Training training = new Training();
        training.setId((Long) trainingData.get("Id"));
        training.setTrainingDate((LocalDate) trainingData.get("trainingDate"));
        training.setTrainingName((String) trainingData.get("trainingName"));
        training.setTrainingDuration( ((Number) trainingData.get("trainingDuration")).longValue() );
        training.setTrainee(traineeService.getTrainee((Long) trainingData.get("TraineeId")));
        training.setTrainer(trainerService.getTrainer((Long) trainingData.get("TrainerId")));
        training.setTrainingType(trainingTypeService.getTraining_Type((Long) trainingData.get("trainingTypeId")));
        return (training.getTrainee() != null && training.getTrainer() != null && training.getTrainingType() != null) ? training : null;
    }

    private User getUser(Map<String, Object> userData) {
        User user = new User();
        user.setId((Long) userData.get("userId"));
        user.setFirstName((String) userData.get("firstName"));
        user.setLastName((String) userData.get("lastName"));
        user.setUserName((String) userData.get("userName"));
        user.setPassword((String) userData.get("password"));
        user.setIsActive((Boolean.parseBoolean(userData.get("isActive").toString())));
        return user;
    }


}
