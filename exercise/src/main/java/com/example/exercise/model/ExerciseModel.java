package com.example.exercise.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer exerciseId;
    @Column(nullable = false)
    private String exerciseName;
    @Column(nullable = false)
    private String exerciseDescription;
    @Column(nullable = false)
    private String exerciseType;
    @Column(nullable = false)
    private String exerciseCategory;
    @Column(nullable = false)
    private String targetBodyArea;
    @Column(nullable = false)
    private String exerciseLevel;
    @Column(nullable = false)
    private String equipmentRequired;
}
