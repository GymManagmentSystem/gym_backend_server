package com.example.exercise.service;


import com.example.exercise.customResponse.ErrorResponse;
import com.example.exercise.customResponse.ExerciseResponse;
import com.example.exercise.customResponse.SuccessResponse;
import com.example.exercise.dto.ExerciseDto;
import com.example.exercise.dto.ExerciseNameListDto;
import com.example.exercise.model.ExerciseModel;
import com.example.exercise.repo.ExerciseRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepo exerciseRepo;

    @Autowired
    private ModelMapper modelMapper;

    public ExerciseService(){}

    public ResponseEntity<ExerciseResponse> getAllExercises() {
        try {
            List<ExerciseModel> exercisesList = exerciseRepo.findAll();
            List <ExerciseDto> responseExerciseList=modelMapper.map(exercisesList, new TypeToken<List<ExerciseDto>>() {}.getType());
            System.out.println("exercise list is"+responseExerciseList);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<ExerciseDto>(responseExerciseList));
        } catch (Exception e) {
            System.out.println("Error is in controller: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    public ResponseEntity<ExerciseResponse> getExercisesNameList() {
        try {
            List<ExerciseModel> exercisesList = exerciseRepo.findAll();
            List <ExerciseNameListDto> responseExerciseList=modelMapper.map(exercisesList, new TypeToken<List<ExerciseNameListDto>>() {}.getType());
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<ExerciseNameListDto>(responseExerciseList));
        } catch (Exception e) {
            System.out.println("Error is in controller: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity <ExerciseResponse> saveExercise(ExerciseDto exerciseDto){
        try{
            String exerciseName=exerciseDto.getExerciseName();
            Boolean exerciseNameExist=exerciseRepo.existByName(exerciseName)==1?true:false;
            if(exerciseNameExist){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Exercise already exists"));
            }else{
                exerciseRepo.save(modelMapper.map(exerciseDto,ExerciseModel.class));
                ExerciseDto latesteExercise=modelMapper.map(exerciseRepo.getLastExercise(),ExerciseDto.class);
                return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<ExerciseDto>(latesteExercise));
            }
        }catch(Exception e){
            System.out.println("Error is in controller: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }

}

    public ResponseEntity<ExerciseResponse> getExerciseCount(){
        try {
            Integer exerciseCount=exerciseRepo.exerciseCount();
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<Integer>(exerciseCount));
        } catch (Exception e) {
            System.out.println("Error is in controller: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }
}