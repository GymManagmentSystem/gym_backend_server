package com.example.schedule.controller;

import com.example.schedule.customResponse.ErrorResponse;
import com.example.schedule.customResponse.ScheduleResponse;
import com.example.schedule.customResponse.SuccessResponse;
import com.example.schedule.dto.ScheduleExerciseDto;
import com.example.schedule.service.SchdeuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/schedules/{memberId}")
public class SheduleController {

    @Autowired
    SchdeuleService schdeuleService;

    @PostMapping(value = "")
    public ResponseEntity<ScheduleResponse> addNewSchedule(@RequestBody ScheduleExerciseDto scheduleExerciseDto) {
        try{
            ScheduleExerciseDto savedScheduleExerciseDto= schdeuleService.addNewSchedule(scheduleExerciseDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<ScheduleExerciseDto>(savedScheduleExerciseDto));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }


}
