package com.gymepam.service.storage;

import com.gymepam.domain.*;
import com.gymepam.service.*;
import com.gymepam.service.util.generatePassword;
import com.gymepam.service.util.generateUserName;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class InitializeStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitializeStorageService.class);

    private Map<String, Map<Long, Long>> idsMap = new HashMap<>();

    @Value("${initializationStorageFile.path}")
    private String excelFilePath;

    private final StorageService storageService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;
    private final UserService userService;
    private final generatePassword genPassword;
    private final generateUserName genUserName;

    @Autowired
    public InitializeStorageService(
            StorageService storageService,
            TraineeService traineeService,
            TrainerService trainerService,
            TrainingService trainingService,
            TrainingTypeService trainingTypeService,
            UserService userService,
            @Qualifier("Gen10Password") generatePassword genPassword,
            @Qualifier("manualUserName") generateUserName genUserName
    ) {
        this.storageService = storageService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.trainingTypeService = trainingTypeService;
        this.userService = userService;
        this.genPassword = genPassword;
        this.genUserName = genUserName;
    }

//    @PostConstruct
    public void initialize() {
        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(excelFilePath))) {
            loadDataFromExcel(workbook);
            storageService.loadDbInMemory();
            LOGGER.info("Data loaded from file");
        } catch (Exception e) {
            LOGGER.error("Error loading data from file", e);
        }
    }

    public void loadDataFromExcel(Workbook workbook) {
        try {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();

                if ("User".equals(sheetName)) {
                    processUserSheet(sheet);
                } else if ("Trainee".equals(sheetName)) {
                    processTraineeSheet(sheet);
                } else if ("Training_Type".equals(sheetName)) {
                    processTrainingTypeSheet(sheet);
                } else if ("Trainer".equals(sheetName)) {
                    processTrainerSheet(sheet);
                } else if ("Training".equals(sheetName)) {
                    processTrainingSheet(sheet);
                } else {
                    LOGGER.warn("Object unknown: {}", sheetName);
                }
            }
            LOGGER.info("File has been uploaded successfully");
        } catch (Exception e) {
            LOGGER.error("Error while File is uploaded", e);
        }
    }
    // Process User sheet
    private void processUserSheet(Sheet sheet) {
        Map<Long, Long> idMap = new HashMap<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            Map<String, Object> userData = extractDataFromRow(sheet.getRow(0), row);
            Long idFieldFileU = (Long) userData.get("userId");
            User user = getUser(userData);
            User result = userService.saveUser(user);
            idMap.put(idFieldFileU, result.getId());
        }
        idsMap.put("User", idMap);
    }
    // Process Trainee sheet
    private void processTraineeSheet(Sheet sheet) {
        Map<Long, Long> idMap = new HashMap<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            Map<String, Object> traineeData = extractDataFromRow(sheet.getRow(0), row);
            Long idFieldFileTrainee = (Long) traineeData.get("Id");
            traineeData.put("userId", idsMap.get("User").get(traineeData.get("userId")));

            Trainee trainee = getDataTrainee(traineeData);
            if (trainee != null) {
                Trainee result = traineeService.saveTrainee(trainee);
                idMap.put(idFieldFileTrainee, result.getId());
            }
        }
        idsMap.put("Trainee", idMap);
    }

    // Process Training_Type sheet
    private void processTrainingTypeSheet(Sheet sheet) {
        Map<Long, Long> idMap = new HashMap<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            Map<String, Object> trainingTypeData = extractDataFromRow(sheet.getRow(0), row);
            Long idFieldFileTrainingType = (Long) trainingTypeData.get("trainingTypeId");

            TrainingType trainingType = getDataTrainingType(trainingTypeData);

            if (trainingType != null) {
                TrainingType result =  trainingTypeService.saveTraining_Type(trainingType);
                idMap.put(idFieldFileTrainingType, result.getId());
            }
        }
        idsMap.put("TrainingType", idMap);
    }

    // Process Trainer sheet
    private void processTrainerSheet(Sheet sheet) {
        Map<Long, Long> idMap = new HashMap<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            Map<String, Object> trainerData = extractDataFromRow(sheet.getRow(0), row);
            Long idFieldFileTrainer = (Long) trainerData.get("Id");

            trainerData.put("userId", idsMap.get("User").get(trainerData.get("userId")));
            trainerData.put("trainingTypeId", idsMap.get("TrainingType").get(trainerData.get("trainingTypeId")));


            Trainer trainer = getDataTrainer(trainerData);
            if (trainer != null) {
                Trainer result =trainerService.saveTrainer(trainer);
                idMap.put(idFieldFileTrainer, result.getId());
            }
        }
        idsMap.put("Trainer", idMap);
    }

    // Process Training sheet
    private void processTrainingSheet(Sheet sheet) {
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            Map<String, Object> trainingData = extractDataFromRow(sheet.getRow(0), row);
            trainingData.put("TraineeId", idsMap.get("Trainee").get(trainingData.get("TraineeId")));
            trainingData.put("TrainerId", idsMap.get("Trainer").get(trainingData.get("TrainerId")));
            trainingData.put("trainingTypeId", idsMap.get("TrainingType").get(trainingData.get("trainingTypeId")));

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
        trainee.setUser(userService.getUser((Long) traineeData.get("userId")));
        return (trainee.getUser() != null) ? trainee : null;
    }

    private Trainer getDataTrainer(Map<String, Object> trainerData) {
        Trainer trainer = new Trainer();
        trainer.setId((Long) trainerData.get("Id"));
        trainer.setTrainingType(trainingTypeService.getTraining_Type((Long) trainerData.get("trainingTypeId")));
        trainer.setUser(userService.getUser((Long) trainerData.get("userId")));
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
        training.setTrainingDuration((Number) trainingData.get("trainingDuration"));
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
        user.setUserName(setUserUserName(user, (String) userData.get("userName")));
        user.setPassword(genPassword.generatePassword());
        user.setIsActive((Boolean.parseBoolean(userData.get("isActive").toString())));
        return user;
    }

    private String setUserUserName(User user, String inputUserName) {
        String userName = genUserName.isValidUsername(inputUserName, user.getFirstName(), user.getLastName()) ?
                inputUserName :
                genUserName.generateUserName(user.getFirstName(), user.getLastName());
        user.setUserName(userName);
        return userName;
    }
}
