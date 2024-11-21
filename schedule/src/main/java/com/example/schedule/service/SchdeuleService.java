package com.example.schedule.service;
import com.example.schedule.dto.ExerciseDto;
import com.example.schedule.dto.ScheduleDto;
import com.example.schedule.dto.ScheduleExerciseDto;
import com.example.schedule.model.ScheduleExerciseModel;
import com.example.schedule.model.ScheduleModel;
import com.example.schedule.repo.ScheduleExerciseRepo;
import com.example.schedule.repo.ScheduleRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchdeuleService {
    @Autowired
    ScheduleRepo scheduleRepo;

    @Autowired
    ScheduleExerciseRepo scheduleExerciseRepo;

    @Autowired
    ModelMapper modelMapper;

    public ScheduleExerciseDto addNewSchedule(ScheduleExerciseDto scheduleExerciseDto) {
        ScheduleExerciseDto savedScheduleDto = new ScheduleExerciseDto();

        try{
            ScheduleModel scheduleModel =scheduleRepo.save(modelMapper.map(scheduleExerciseDto.getSchedule(), ScheduleModel.class));
            ScheduleDto scheduleDto = modelMapper.map(scheduleModel, ScheduleDto.class);
            System.out.println("schdeule dto is " + scheduleDto);
            savedScheduleDto.setSchedule(scheduleDto);
            assert scheduleModel != null;

            List<ExerciseDto> exerciseList=scheduleExerciseDto.getExerciseList();
            System.out.println(scheduleDto.getScheduleId());
            List<ScheduleExerciseModel> scheduleExerciseModelList=new ArrayList<>();

            for(ExerciseDto exerciseDto:exerciseList){
                ScheduleExerciseModel scheduleExerciseModel=new ScheduleExerciseModel();
                scheduleExerciseModel.setScheduleId(scheduleDto.getScheduleId());
                scheduleExerciseModel.setExerciseName(exerciseDto.getExerciseName());
                scheduleExerciseModel.setReps(exerciseDto.getReps());
                scheduleExerciseModel.setSets(exerciseDto.getSets());
                scheduleExerciseModelList.add(scheduleExerciseModel);
            }
            List<ScheduleExerciseModel> savedSheduleExerciseModelList=scheduleExerciseRepo.saveAll(scheduleExerciseModelList);
            List<ExerciseDto> savedScheduleExerciseDtoList=modelMapper.map(savedSheduleExerciseModelList,new TypeToken<List<ExerciseDto>>(){}.getType());
            savedScheduleDto.setExerciseList(savedScheduleExerciseDtoList);
            System.out.println("saved schedule data: "+savedScheduleDto);
            return savedScheduleDto;

        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }




}
