package com.example.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {
    private int memberId;
    private int scheduleId;
    private String scheduleType;
    private String scheduleDay1;
    private String scheduleDay2;
    private String scheduleDays;
    private String scheduleDescription;
    private String scheduleExpirayDate;
    private String scheduleRegisteredDate;
    private String scheduleValidTime;
    private boolean isActive;
}
