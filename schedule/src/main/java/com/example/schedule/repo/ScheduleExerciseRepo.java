package com.example.schedule.repo;

import com.example.schedule.model.ScheduleExerciseModel;
import com.example.schedule.model.ScheduleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ScheduleExerciseRepo extends JpaRepository<ScheduleExerciseModel, Long>{
    @Query(value="SELECT * FROM schedule_exercise_model WHERE schedule_id=?1",nativeQuery = true)
    List<ScheduleExerciseModel> getExercisesListByScheduleId(Integer scheduleId);
}
