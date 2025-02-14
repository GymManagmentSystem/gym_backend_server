package com.example.schedule.service;
import com.example.exercise.customResponse.ExerciseResponse;
import com.example.exercise.customResponse.SuccessResponse;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchdeuleService {

    private WebClient exerciseWebClient;

    @Autowired
    private ScheduleRepo scheduleRepo;

    @Autowired
    private ScheduleExerciseRepo scheduleExerciseRepo;

    @Autowired
    private ModelMapper modelMapper;

    public SchdeuleService(WebClient ExerciseWebClient){
        this.exerciseWebClient = ExerciseWebClient;
    }


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
                    scheduleExerciseModel.setDuration(exerciseDto.getDuration());
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

    public List<ScheduleExerciseDto> getSchedulesById(Integer memberId,boolean isActive,String jwtToken) {
        try{
            List<ScheduleExerciseDto> scheduleExerciseList=new ArrayList<>();
            List<ScheduleModel> currentScheduleList=scheduleRepo.getSchedulesById(memberId,isActive);
            List<com.example.exercise.dto.ExerciseDto> exerciseList=getExercisesList(jwtToken);

            for(ScheduleModel scheduleModel:currentScheduleList){

                ScheduleExerciseDto scheduleExerciseDto=new ScheduleExerciseDto();
                scheduleExerciseDto.setSchedule(modelMapper.map(scheduleModel, ScheduleDto.class));
                List<ScheduleExerciseModel> scheduleExerciseModel=scheduleExerciseRepo.getExercisesListByScheduleId(scheduleModel.getScheduleId());
                scheduleExerciseDto.setExerciseList(modelMapper.map(scheduleExerciseModel,new TypeToken<List<ExerciseDto>>(){}.getType()));

                for(ExerciseDto exercise:scheduleExerciseDto.getExerciseList()){

                    exercise.setExerciseUrl(getExerciseImageUrlByName(exercise.getExerciseName(),exerciseList));
                }
                scheduleExerciseList.add(scheduleExerciseDto);
            }
            System.out.println("Schedule exercise list:"+memberId+" : "+scheduleExerciseList);
            getExercisesList(jwtToken);
            return scheduleExerciseList;

        }catch(Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    public List<com.example.exercise.dto.ExerciseDto> getExercisesList(String jwtToken){
        try{
            ResponseEntity<SuccessResponse<com.example.exercise.dto.ExerciseDto>> paymentInfo = exerciseWebClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/exercises/").build())
                    .headers(headers -> headers.setBearerAuth(jwtToken))
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<SuccessResponse<com.example.exercise.dto.ExerciseDto>>() {})
                    .block();
            assert paymentInfo.getBody() != null;
            return paymentInfo.getBody().getDataList();
        }catch(Exception e){
            System.out.println("error is sending http web client to exercise ms");
            throw new RuntimeException("Error in loading exercises");

        }

    }

    public String getExerciseImageUrlByName(String exerciseName, List<com.example.exercise.dto.ExerciseDto> exerciseList){
        for(com.example.exercise.dto.ExerciseDto exerciseDto:exerciseList){
            if(exerciseDto.getExerciseName().equals(exerciseName)){
                return exerciseDto.getExerciseImageUrl();
            }
        }
        return "No url found";
    }


}
