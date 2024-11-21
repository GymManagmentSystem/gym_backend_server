package com.example.schedule.repo;

import com.example.schedule.model.ScheduleExerciseModel;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ScheduleExerciseRepo extends JpaRepository<ScheduleExerciseModel, Long>{
}
