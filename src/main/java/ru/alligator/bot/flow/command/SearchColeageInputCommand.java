package ru.alligator.bot.flow.command;

import static ru.alligator.bot.flow.command.CommandConstants.USER_INPUT_COMMAND_NAME;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;
import ru.alligator.bot.flow.EntryBot;
import ru.alligator.bot.flow.TgUtils;
import ru.alligator.bot.flow.stage.Stage;
import ru.alligator.bot.storage.ChatState;

@Component
@RequiredArgsConstructor
public class SearchColeageInputCommand implements Command {

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
        return getName().equals(commandName) && Stage.ENTER_SEARCH_STRING.equals(chatState.getCurrentStage());
    }

    @Override
    public void acceptMessage(String searchStr, ChatState chatState, EntryBot sender) {
        if (searchStr != null) {
            //TODO: поиск сотрудника
            TgUtils.sendMessage(chatState.getChatId().toString(),
                "Иванов Иван Иванович +7(987)654-32-11", sender);
        }
        Command.enterStage(Stage.AUTH_MAIN_MENU, chatState, sender);
    }
}
