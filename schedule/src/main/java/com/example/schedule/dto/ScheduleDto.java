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
    private String startDate;
    private boolean isActive;
}
