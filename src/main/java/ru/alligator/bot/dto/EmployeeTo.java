package ru.alligator.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * To сотрудника.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeTo {

    /**
     * id.
     */
    private UUID id;

    private String lastName;

    private String firstName;

    private String patronymic;

    private String phone;

    private String email;

    private String position;

    private String positionName;

    private UUID departmentId;

    private String departmentName;
}
