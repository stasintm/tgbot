package ru.alligator.bot.flow.command;

import static ru.alligator.bot.flow.command.CommandConstants.SEARCH_COMMAND_NAME;
import static ru.alligator.bot.flow.command.CommandConstants.SEARCH_COMMAND_TITLE;

import org.springframework.stereotype.Component;
import ru.alligator.bot.flow.EntryBot;
import ru.alligator.bot.flow.stage.Stage;
import ru.alligator.bot.storage.ChatState;

import java.util.List;

@Component
public class SearchColleagueCommand implements Command {

    public static final String NAME = "searchColleague";

    @Override
    public String getName() {
        return SEARCH_COMMAND_NAME;
    }

    @Override
    public String getLabel() {
        return SEARCH_COMMAND_TITLE;
    }

    @Override
    public List<Stage> getKnownStages() {
        return List.of(
            Stage.AUTH_MAIN_MENU
        );
    }

    @Override
    public void acceptMessage(String message, ChatState chatState, EntryBot sender) {
        Command.enterStage(Stage.ENTER_SEARCH_STRING, chatState, sender);
    }
}
