package com.example.schedule.repo;

import com.example.schedule.model.ScheduleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ScheduleRepo extends JpaRepository<ScheduleModel,Integer> {
    @Modifying
    @Query(value = "UPDATE schedule_model SET is_active=false WHERE is_active=true AND member_id=?1 AND schedule_type=?2", nativeQuery = true)
    Integer setIsActiveFalse(Integer memberId,String scheduleType);

    @Query(value = "SELECT schedule_id FROM schedule_model WHERE is_active=true AND member_id=?1 AND schedule_type=?2", nativeQuery = true)
    List<Integer> findFirstSchedules(Integer memberId,String scheduleType);

    @Query(value="SELECT schedule_id,member_id,schedule_type,start_date,is_active FROM schedule_model WHERE member_id=?1 AND is_active=?2",nativeQuery = true)
    List<ScheduleModel> getSchedulesById(Integer memberId,boolean isActive);

}
