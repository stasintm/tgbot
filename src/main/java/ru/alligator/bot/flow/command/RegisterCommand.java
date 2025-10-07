package ru.alligator.bot.flow.command;

import static ru.alligator.bot.flow.command.CommandConstants.REGISTER_COMMAND_NAME;
import static ru.alligator.bot.flow.command.CommandConstants.REGISTER_COMMAND_TITLE;

import org.springframework.stereotype.Component;
import ru.alligator.bot.flow.EntryBot;
import ru.alligator.bot.flow.stage.Stage;
import ru.alligator.bot.storage.ChatState;

import java.util.List;

@Component
public class RegisterCommand implements Command {

    public String getName() {
        return REGISTER_COMMAND_NAME;
    }

    @Override
    public String getLabel() {
        return REGISTER_COMMAND_TITLE;
    }

    @Override
    public List<Stage> getKnownStages() {
        return List.of(Stage.NOT_AUTHORIZED);
    }

    @Override
    public void acceptMessage(String entries, ChatState chatState, EntryBot sender) {
        Command.enterStage(Stage.REG_NUMBER, chatState, sender);
    }
}
