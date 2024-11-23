package com.example.schedule.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ScheduleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int scheduleId;
    private int memberId;
    private String scheduleType;
    private String startDate;
    @Column(columnDefinition = "boolean default true")
    private boolean isActive=true;

}
