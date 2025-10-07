package com.example.NYA.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class FilterDto {
    private LocalTime startTime;
    private LocalTime endTime;
}
