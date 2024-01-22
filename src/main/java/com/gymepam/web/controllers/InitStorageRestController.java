package com.gymepam.web.controllers;

import com.gymepam.dao.*;
import com.gymepam.service.*;
import com.gymepam.service.storage.InitializeStorageService;
import com.gymepam.service.storage.StorageService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/initStorageRealDb")
public class InitStorageRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitStorageRestController.class);

    @Value("${initializationStorageFile.path}")
    private String excelFilePath;
    private InitializeStorageService initializeStorageService;
    private StorageService storageService;

    @Autowired
    public InitStorageRestController(InitializeStorageService initializeStorageService, StorageService storageService) {
        this.initializeStorageService = initializeStorageService;
        this.storageService = storageService;
    }


    @GetMapping
    public ResponseEntity<Map<String, List<Object>>> getTrainee(){
        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(excelFilePath))) {
            initializeStorageService.loadDataFromExcel(workbook);
            storageService.loadDbInMemory();
            LOGGER.info("Data loaded from file in Real db");
        } catch (Exception e) {
            LOGGER.error("Error loading data from file", e);
        }
        return new ResponseEntity<>(storageService.getDbInMemory(), HttpStatus.OK);
    }



}
