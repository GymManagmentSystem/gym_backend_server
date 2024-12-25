package com.example.exercise.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseNameListDto {
    private String exerciseName;
    private String exerciseType;
    private String exerciseUnit;
}
