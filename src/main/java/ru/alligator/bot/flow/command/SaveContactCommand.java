package ru.alligator.bot.flow.command;

import static ru.alligator.bot.flow.command.CommandConstants.USER_INPUT_COMMAND_NAME;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;
import ru.alligator.bot.flow.EntryBot;
import ru.alligator.bot.flow.TgUtils;
import ru.alligator.bot.flow.stage.Stage;
import ru.alligator.bot.storage.ChatState;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SaveContactCommand implements Command {

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
        return getName().equals(commandName) && Stage.REG_NUMBER.equals(chatState.getCurrentStage());
    }

    @Override
    public void acceptMessage(String phoneNumber, ChatState chatState, EntryBot sender) {
        if (phoneNumber != null) {
            chatState.setPhone(phoneNumber);
            TgUtils.sendMessage(chatState.getChatId().toString(), "Номер принят", sender);
            //TODO: проверка номера, получение employeeId
            chatState.setEmployeeId(UUID.randomUUID());
        }
        if (!chatState.isApproved()) {
            TgUtils.sendMessage(chatState.getChatId().toString(), "Пользователь не найден в БД компании!",
                sender);
        }
        Command.enterStage(chatState.isApproved() ? Stage.AUTH_MAIN_MENU : Stage.NOT_AUTHORIZED, chatState, sender);
    }
}
