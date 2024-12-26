package com.example.exercise.controller;
import com.example.exercise.customResponse.ExerciseResponse;
import com.example.exercise.dto.ExerciseDto;
import com.example.exercise.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;



@RestController
@RequestMapping(value = "api/v1/exercises")

//hVE TO DO SERVER SIDE VALIDATIONS
public class ExerciseController {
    @Autowired
    private ExerciseService exerciseService;

    @GetMapping("/")
    public ResponseEntity<ExerciseResponse> getAllExercises(){
        return exerciseService.getAllExercises();
    }

    @GetMapping("/names/")
    public ResponseEntity<ExerciseResponse> getExercisesNameList(){
        return exerciseService.getExercisesNameList();
    }


    @PostMapping("/")
    public ResponseEntity<ExerciseResponse> addExercise(@Valid @RequestBody ExerciseDto exerciseDto) {
            return exerciseService.saveExercise(exerciseDto);
    }

    @GetMapping("/count")
    public ResponseEntity<ExerciseResponse> getExercisesCount(){
        return exerciseService.getExerciseCount();
    }

}
