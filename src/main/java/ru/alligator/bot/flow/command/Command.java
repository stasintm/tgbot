package ru.alligator.bot.flow.command;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.alligator.bot.flow.EntryBot;
import ru.alligator.bot.flow.TgUtils;
import ru.alligator.bot.flow.stage.Stage;
import ru.alligator.bot.storage.ChatState;

import java.util.Collections;
import java.util.List;

public interface Command {

    public static final String COMMAND_PREFIX = "/";

    String getName();

    String getLabel();

    default List<Stage> getKnownStages() {
        return Collections.emptyList();
    }

    default boolean isApplicable(String commandName, ChatState chatState) {
        return (COMMAND_PREFIX + getName()).equalsIgnoreCase(commandName)
            && getKnownStages().contains(chatState.getCurrentStage());
    }

    void acceptMessage(String msg, ChatState chatState, EntryBot sender);

    static void enterStage(Stage newStage, ChatState chatState, EntryBot sender) {
        try {
            TgUtils.drawMenu(
                chatState,
                newStage.getMaxButtonsInRow(),
                newStage.getHeader(),
                newStage.getButtons(),
                sender
            );
            chatState.setCurrentStage(newStage);
        } catch (TelegramApiException ex) {
            throw new RuntimeException(ex);
        }
    }



}
