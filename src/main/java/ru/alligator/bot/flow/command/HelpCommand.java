package ru.alligator.bot.flow.command;

import static ru.alligator.bot.flow.command.CommandConstants.HELP_COMMAND_NAME;
import static ru.alligator.bot.flow.command.CommandConstants.HELP_COMMAND_TITLE;

import org.springframework.stereotype.Component;
import ru.alligator.bot.flow.EntryBot;
import ru.alligator.bot.flow.TgUtils;
import ru.alligator.bot.flow.stage.Stage;
import ru.alligator.bot.storage.ChatState;

import java.util.List;

@Component
public class HelpCommand implements Command {

    @Override
    public String getName() {
        return HELP_COMMAND_NAME;
    }

    @Override
    public String getLabel() {
        return HELP_COMMAND_TITLE;
    }

    @Override
    public List<Stage> getKnownStages() {
        return List.of(Stage.NOT_AUTHORIZED, Stage.AUTH_MAIN_MENU);
    }

    @Override
    public void acceptMessage(String msg, ChatState chatState, EntryBot sender) {
        String info = chatState.getEmployeeId() != null ?
            """
                В боте вам доступен поиск по базе контактов сотрудников и работа с вашими отпусками (просмотр и создание новых).                            
            """ :
            """
                *Регистрация в сервисе производится в два этапа:*
                1. Вы нажимаете "Регистрация"
                2. Вы предоставляете свой контакт. Если он есть в БД сотрудников, то вы переходите в режим работы с ботом как зарегистрированный пользователь
                """;

        TgUtils.sendMessage(chatState.getChatId().toString(), info, sender);
        Command.enterStage(chatState.getCurrentStage(), chatState, sender);
    }
}
