package com.gymepam.web.controllers;

import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.service.facade.TrainingFacadeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Training Controller", value = "Operations for creating Trainings in the application")
@AllArgsConstructor
@RestController
@RequestMapping("/training")
public class TrainingRestController {

    TrainingFacadeService trainingFacade;

    @ApiOperation(value = "Save Training", notes = "Register a new Training in the system")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
    @PostMapping
    public ResponseEntity saveTraining(@RequestBody @Validated TrainingRecord.TrainingRequest trainingRequest){
        return trainingFacade.saveTraining_(trainingRequest);
    }


}
