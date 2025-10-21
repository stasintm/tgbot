package ru.alligator.bot.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.alligator.bot.flow.stage.Stage;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatState {
    private Long chatId;
    private String phone;
    private Stage currentStage;
    private UUID employeeId;
    private LocalDate startVacationDate;
    private LocalDate endVacationDate;

    public boolean isApproved() {
        return employeeId != null;
    }

    public void resetVacationDates() {
        startVacationDate = null;
        endVacationDate = null;
    }
}
