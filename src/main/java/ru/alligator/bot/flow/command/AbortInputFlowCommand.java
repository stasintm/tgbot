package ru.alligator.bot.flow.command;

import static ru.alligator.bot.flow.command.CommandConstants.ABORT_COMMAND_NAME;
import static ru.alligator.bot.flow.command.CommandConstants.ABORT_COMMAND_TITLE;
import static ru.alligator.bot.flow.stage.Stage.ENTER_SEARCH_STRING;

import org.springframework.stereotype.Component;
import ru.alligator.bot.flow.EntryBot;
import ru.alligator.bot.flow.stage.Stage;
import ru.alligator.bot.storage.ChatState;

import java.util.List;

@Component
public class AbortInputFlowCommand implements Command {


    @Override
    public String getName() {
        return ABORT_COMMAND_NAME;
    }

    @Override
    public String getLabel() {
        return ABORT_COMMAND_TITLE;
    }

    @Override
    public List<Stage> getKnownStages() {
        return List.of(
            ENTER_SEARCH_STRING
        );
    }

    @Override
    public void acceptMessage(String message, ChatState chatState, EntryBot sender) {
        if (ENTER_SEARCH_STRING.equals(chatState.getCurrentStage())) {
            Command.enterStage(Stage.AUTH_MAIN_MENU, chatState, sender);
        }
    }
}