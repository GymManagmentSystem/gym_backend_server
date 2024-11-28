package com.example.schedule.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleExerciseDto {
    private ScheduleDto schedule;
    private List<ExerciseDto> exerciseList;
}
