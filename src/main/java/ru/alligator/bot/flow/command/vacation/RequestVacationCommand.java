package ru.alligator.bot.flow.command.vacation;

import org.springframework.stereotype.Component;
import ru.alligator.bot.flow.EntryBot;
import ru.alligator.bot.flow.command.Command;
import ru.alligator.bot.flow.command.CommandConstants;
import ru.alligator.bot.flow.stage.Stage;
import ru.alligator.bot.storage.ChatState;

import java.util.List;

@Component
public class RequestVacationCommand implements Command {

    @Override
    public String getName() {
        return CommandConstants.REQUEST_VACATION_COMMAND_NAME;
    }

    @Override
    public String getLabel() {
        return CommandConstants.REQUEST_VACATION_COMMAND_TITLE;
    }

    @Override
    public List<Stage> getKnownStages() {
        return List.of(
            Stage.AUTH_MAIN_MENU
        );
    }

    @Override
    public void acceptMessage(String searchStr, ChatState chatState, EntryBot sender) {
        chatState.resetVacationDates();
        Command.enterStage(Stage.ENTER_START_DATE, chatState, sender);
    }
}
