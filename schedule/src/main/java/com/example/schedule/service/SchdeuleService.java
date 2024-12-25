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
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(rollbackFor = Exception.class)
    public ScheduleExerciseDto addNewSchedule(ScheduleExerciseDto scheduleExerciseDto) {
        try{
                ScheduleExerciseDto savedScheduleDto = new ScheduleExerciseDto();
                scheduleExerciseDto.getSchedule().setActive(true);
                Integer invalidatePreviousSchedules=scheduleRepo.setIsActiveFalse(scheduleExerciseDto.getSchedule().getMemberId(),scheduleExerciseDto.getSchedule().getScheduleType());
                if(invalidatePreviousSchedules != 1){
                    List<Integer> initialScheduleList=scheduleRepo.findFirstSchedules(scheduleExerciseDto.getSchedule().getMemberId(),scheduleExerciseDto.getSchedule().getScheduleType());
                    assert initialScheduleList != null;
                    if (!initialScheduleList.isEmpty()){
                        throw new RuntimeException("SUF"); //SCHEDULE UPDATION FAILED
                    }
                }
                ScheduleModel scheduleModel =scheduleRepo.save(modelMapper.map(scheduleExerciseDto.getSchedule(), ScheduleModel.class));

                ScheduleDto scheduleDto = modelMapper.map(scheduleModel, ScheduleDto.class);
                savedScheduleDto.setSchedule(scheduleDto);
                assert scheduleModel != null;

                List<ExerciseDto> exerciseList=scheduleExerciseDto.getExerciseList();
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

            return savedScheduleDto;


        }catch (InvalidDataAccessResourceUsageException e){
            System.out.println((e.getMessage()));
            throw new RuntimeException("IDARUE");//invalidate Data Access Resource
        }
        catch(Exception e){
            System.out.println((e.getMessage()));
            throw new RuntimeException(e.getMessage());
        }

    }

    public List<ScheduleExerciseDto> getSchedulesById(Integer memberId,boolean isActive) {
        try{
            List<ScheduleExerciseDto> scheduleExerciseList=new ArrayList<>();
            List<ScheduleModel> currentScheduleList=scheduleRepo.getSchedulesById(memberId,isActive);
            for(ScheduleModel scheduleModel:currentScheduleList){
                ScheduleExerciseDto scheduleExerciseDto=new ScheduleExerciseDto();
                scheduleExerciseDto.setSchedule(modelMapper.map(scheduleModel, ScheduleDto.class));
                List<ScheduleExerciseModel> scheduleExerciseModel=scheduleExerciseRepo.getExercisesListByScheduleId(scheduleModel.getScheduleId());
                scheduleExerciseDto.setExerciseList(modelMapper.map(scheduleExerciseModel,new TypeToken<List<ExerciseDto>>(){}.getType()));
                scheduleExerciseList.add(scheduleExerciseDto);
            }
            return scheduleExerciseList;

        }catch(Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    


}
