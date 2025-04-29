package com.url.shortener.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class clickEventDTO {
    private LocalDate clickDate;
    private Long count;
}

//This DTO is essential for returning user-friendly API responses while keeping database entities hidden from the frontend.