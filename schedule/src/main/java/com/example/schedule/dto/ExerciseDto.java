package com.example.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDto {
    private String exerciseName;
    private String reps;
    private String exerciseUrl;
    private int sets;
    private int duration;
}
