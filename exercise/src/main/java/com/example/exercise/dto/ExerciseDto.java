package com.example.exercise.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseDto {
    private Integer exerciseId;


    @NotNull(message = "Exercise Name Should Not Be Null")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Only alphanumeric characters and spaces are allowed for equipment.")
    private String exerciseName;

    @NotNull(message = "Exercise Name Should Not Be Null")
    private String exerciseDescription;

    @NotNull(message = "Exercise Name Should Not Be Null")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Only alphanumeric characters and spaces are allowed for equipment.")
    private String exerciseType;

    @NotNull(message = "Exercise Name Should Not Be Null")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Only alphanumeric characters and spaces are allowed for equipment.")
    private String exerciseCategory;

    @NotNull(message = "Exercise Name Should Not Be Null")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Only alphanumeric characters and spaces are allowed for equipment.")
    private String targetBodyArea;

    @NotNull(message = "Exercise Name Should Not Be Null")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Only alphanumeric characters and spaces are allowed for equipment.")
    private String exerciseLevel;

    @NotNull(message = "Exercise Name Should Not Be Null")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Only alphanumeric characters and spaces are allowed for equipment.")
    private String exerciseEquipment;

    private String exerciseImageUrl;

    private String exerciseUnit;
}
