package com.example.schedule.repo;

import com.example.schedule.model.ScheduleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface ScheduleRepo extends JpaRepository<ScheduleModel,Integer> {
}
