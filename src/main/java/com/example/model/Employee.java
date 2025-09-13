package com.example.model;

import java.time.LocalDate;

public record Employee(Long id, LocalDate dateFrom, LocalDate dateTo) {
}
