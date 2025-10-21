package ru.alligator.bot.flow.command.vacation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.alligator.bot.api.VacationFeignClient;
import ru.alligator.bot.dto.VacationTo;
import ru.alligator.bot.flow.EntryBot;
import ru.alligator.bot.flow.TgUtils;
import ru.alligator.bot.flow.command.Command;
import ru.alligator.bot.flow.command.CommandConstants;
import ru.alligator.bot.flow.stage.Stage;
import ru.alligator.bot.storage.ChatState;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ConfirmVacationCommand implements Command {

    private final VacationFeignClient vacationFeignClient;

    @Override
    public String getName() {
        return CommandConstants.CONFIRM_VACATION_COMMAND_NAME;
    }

    @Override
    public String getLabel() {
        return CommandConstants.CONFIRM_VACATION_COMMAND_TITLE;
    }

    @Override
    public List<Stage> getKnownStages() {
        return List.of(
            Stage.VACATION_CONFIRMATION
        );
    }

    @Override
    public void acceptMessage(String searchStr, ChatState chatState, EntryBot sender) {
        var vacation = VacationTo.builder()
            .withEmployeeId(chatState.getEmployeeId())
            .withStartDate(chatState.getStartVacationDate())
            .withEndDate(chatState.getEndVacationDate())
            .build();
        vacationFeignClient.createVacation(vacation);

        TgUtils.sendMessage(chatState.getChatId().toString(),
                String.format(
                        "Отпуск с %s по %s успешно создан!",
                        chatState.getStartVacationDate(), chatState.getEndVacationDate()),
            sender);

        chatState.resetVacationDates();
        Command.enterStage(Stage.AUTH_MAIN_MENU, chatState, sender);
    }
}
