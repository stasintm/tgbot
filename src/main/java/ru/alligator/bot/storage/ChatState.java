package ru.alligator.bot.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.alligator.bot.flow.stage.Stage;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatState {
    private Long chatId;
    private String phone;
    private Stage currentStage;
    private UUID employeeId;

    public boolean isApproved() {
        return employeeId != null;
    }
}
