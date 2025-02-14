package com.example.schedule.controller;

import com.example.schedule.customResponse.ErrorResponse;
import com.example.schedule.customResponse.ScheduleResponse;
import com.example.schedule.customResponse.SuccessResponse;
import com.example.schedule.dto.ScheduleExerciseDto;
import com.example.schedule.service.SchdeuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/schedules")
public class SheduleController {

    @Autowired
    SchdeuleService schdeuleService;

    @PostMapping(value = "/{memberId}")
    public ResponseEntity<ScheduleResponse> addNewSchedule(@RequestBody ScheduleExerciseDto scheduleExerciseDto) {
        try{
            System.out.println("Schedule list"+ scheduleExerciseDto);
            ScheduleExerciseDto savedScheduleExerciseDto= schdeuleService.addNewSchedule(scheduleExerciseDto);
            System.out.println("Schedule list"+ savedScheduleExerciseDto);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<ScheduleExerciseDto>(savedScheduleExerciseDto));
        }catch(Exception e){
            if(e.getMessage().equals("SUF")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Schedule Update Failed"));
            }else if(e.getMessage().equals("IDARUE")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalidate Data Access Resource"));
            }
            else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
            }

        }
    }

    @GetMapping(value = "/current/{memberId}")
    public ResponseEntity<ScheduleResponse> getCurrentSchedule(@PathVariable("memberId") String memberId, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String jwtToken = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7); // Remove "Bearer " prefix
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Authorization header is missing or invalid"));
        }
        try{
            //if isActive = 1 it shows current schedules
            int id=Integer.parseInt(memberId);
            List<ScheduleExerciseDto> scheduleList=schdeuleService.getSchedulesById(id,true,jwtToken);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<ScheduleExerciseDto>(scheduleList));
        }catch(Exception e){
            System.out.println("Exception is "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping(value = "/past/{memberId}")
    public ResponseEntity<ScheduleResponse> getPastHistorySchedule(@PathVariable("memberId") String memberId,@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String jwtToken = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7); // Remove "Bearer " prefix
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Authorization header is missing or invalid"));
        }


        try{
            //if isActive = 0 it shows current pastSchedules
            int id=Integer.parseInt(memberId);
            List<ScheduleExerciseDto> scheduleList=schdeuleService.getSchedulesById(id,false,jwtToken);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<ScheduleExerciseDto>(scheduleList));
        }catch(Exception e){
            System.out.println("Exception is "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }


}
