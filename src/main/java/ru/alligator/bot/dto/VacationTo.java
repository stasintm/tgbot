package ru.alligator.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VacationTo {
    private UUID id;

    private UUID employeeId;

    private LocalDate startDate;

    private LocalDate endDate;

    private VacationStatus status;
}
