package ru.alligator.bot.flow.command.vacation;

import static ru.alligator.bot.flow.TgUtils.DATE_TIME_FORMATTER;
import static ru.alligator.bot.flow.command.CommandConstants.USER_INPUT_COMMAND_NAME;
import static ru.alligator.bot.flow.stage.Stage.ENTER_END_DATE;
import static ru.alligator.bot.flow.stage.Stage.VACATION_CONFIRMATION;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;
import ru.alligator.bot.api.EmployeeFeignClient;
import ru.alligator.bot.flow.EntryBot;
import ru.alligator.bot.flow.TgUtils;
import ru.alligator.bot.flow.command.Command;
import ru.alligator.bot.flow.stage.Stage;
import ru.alligator.bot.storage.ChatState;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VacationDateInputCommand implements Command {

    private final EmployeeFeignClient employeeFeignClient;

    @Override
    public String getName() {
        return USER_INPUT_COMMAND_NAME;
    }

    @Override
    public String getLabel() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isApplicable(String commandName, ChatState chatState) {
        return getName().equals(commandName)
            && List.of(Stage.ENTER_START_DATE, ENTER_END_DATE).contains(chatState.getCurrentStage());
    }

    @Override
    public void acceptMessage(String inputStr, ChatState chatState, EntryBot sender) {
        LocalDate date;
        try {
            date = LocalDate.parse(inputStr, DATE_TIME_FORMATTER);
        } catch (Exception ex) {
            date = null;
        }
        if (date == null) {
            TgUtils.sendMessage(
                chatState.getChatId().toString(),
                "Ожидается корректная дата. Попробуйте еще раз",
                sender
            );
            Command.enterStage(chatState.getCurrentStage(), chatState, sender);
            return;
        }

        if (Stage.ENTER_START_DATE.equals(chatState.getCurrentStage())) {
            chatState.setStartVacationDate(date);
            Command.enterStage(ENTER_END_DATE, chatState, sender);
        } else {
            chatState.setEndVacationDate(date);

            TgUtils.sendMessage(chatState.getChatId().toString(),
                String.format(
                    "Вы собираетесь создать отпуск с %s по %s.",
                    DATE_TIME_FORMATTER.format(chatState.getStartVacationDate()),
                    DATE_TIME_FORMATTER.format(chatState.getEndVacationDate())), sender);

            Command.enterStage(VACATION_CONFIRMATION, chatState, sender);
        }
    }
}
