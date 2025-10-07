package ru.alligator.bot.flow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.alligator.bot.flow.command.Command;
import ru.alligator.bot.flow.command.CommandConstants;
import ru.alligator.bot.flow.stage.Stage;
import ru.alligator.bot.storage.ChatState;
import ru.alligator.bot.storage.ChatStateStorage;

import java.util.List;

@Slf4j
@Component
public class EntryBot extends TelegramLongPollingBot {

    private final String name;
    private final ChatStateStorage storage;
    private final List<Command> commands;

    public EntryBot(ChatStateStorage storage,
                    List<Command> commands,
                    @Value("${bot.name}") String name,
                    @Value("${bot.token}") String token) {
        super(token);
        this.name = name;
        this.storage = storage;
        this.commands = commands;
    }

    private void clearButton(String chatId, Integer messageId) {
        try {
            EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
            editMarkup.setChatId(chatId);
            editMarkup.setMessageId(messageId);
            editMarkup.setReplyMarkup(null);
            execute(editMarkup);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId;
        String msg = "";
        String cmdName = CommandConstants.USER_INPUT_COMMAND_NAME;
        final ChatState state;

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            state = storage.getState(chatId);
            msg = update.getMessage().getText();
            if (msg != null && msg.startsWith(Command.COMMAND_PREFIX)) {
                cmdName = msg;
            } else if (update.getMessage().getContact() != null) {
                msg = update.getMessage().getContact().getPhoneNumber();
            }
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            state = storage.getState(chatId);
            msg = update.getCallbackQuery().getData();
            if (msg != null && msg.startsWith(Command.COMMAND_PREFIX)) {
                cmdName = msg;
            }
            clearButton(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
        } else {
            log.error("Unsupport action!");
            return;
        }

        final String cmdNameFinal = cmdName;
        var command = commands.stream().filter(c -> c.isApplicable(cmdNameFinal, state)).findFirst().orElse(null);
        if (command != null) {
            command.acceptMessage(msg, state, this);
            storage.putState(state);
            return;
        }
        Command.enterStage(state.isApproved() ? Stage.AUTH_MAIN_MENU : Stage.NOT_AUTHORIZED, state, this);
        storage.putState(state);
    }

    @Override
    public String getBotUsername() {
        return name;
    }
}
