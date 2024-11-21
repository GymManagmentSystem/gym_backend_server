package com.example.exercise.repo;

import com.example.exercise.model.ExerciseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ExerciseRepo extends JpaRepository<ExerciseModel,String> {
    @Query(value = "SELECT * FROM exercise_model ORDER BY exercise_Id DESC LIMIT 1",nativeQuery = true)
    ExerciseModel getLastExercise();

    @Query(value = "SELECT COUNT(*)>0 FROM exercise_model WHERE exercise_name=?1",nativeQuery = true)
    Integer existByName(String name);

}
