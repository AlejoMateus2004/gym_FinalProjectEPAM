package com.gymepam.web.controllers;

import com.gymepam.domain.entities.TrainingType;
import com.gymepam.service.TrainingTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Training Type Controller", value = "Operations for retrieving Training Types in the application")
@AllArgsConstructor
@RestController
@RequestMapping("/training-type")
public class TrainingTypeRestController {

    TrainingTypeService trainingTypeService;

    @ApiOperation(value = "Get Training Type List", notes = "Get All Training Types")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
    @GetMapping("/all")
    public ResponseEntity<List<TrainingType>> getTrainingTypeList(){
        return new ResponseEntity<>(trainingTypeService.getAllTrainingTypes(),HttpStatus.CREATED);
    }


}
