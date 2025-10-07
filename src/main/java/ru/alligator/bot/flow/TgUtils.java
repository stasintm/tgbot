package ru.alligator.bot.flow;

import static ru.alligator.bot.flow.command.Command.COMMAND_PREFIX;
import static ru.alligator.bot.flow.command.CommandConstants.SEND_CONTACT_COMMAND_NAME;

import org.apache.commons.lang3.tuple.Pair;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.alligator.bot.storage.ChatState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TgUtils {

    public static void sendMessage(String chatId, String msg, EntryBot sender) {
        var sendMessage = new SendMessage(chatId, msg);
        sendMessage.enableMarkdown(true);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static ReplyKeyboard drawContactMenu(
                                        List<Pair<String, String>> menuSource) throws TelegramApiException {
        ReplyKeyboardMarkup keyBoardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = menuSource
            .stream()
            .map(p -> {
                KeyboardButton button = new KeyboardButton();
                if (p.getKey().equals(SEND_CONTACT_COMMAND_NAME)) {
                    button.setRequestContact(true);
                }
                button.setText(p.getValue());
                KeyboardRow row = new KeyboardRow();
                row.add(button);
                return row;
            })
            .collect(Collectors.toList());

        keyBoardMarkup.setKeyboard(rows);
        keyBoardMarkup.setResizeKeyboard(true);
        keyBoardMarkup.setOneTimeKeyboard(true);

        return keyBoardMarkup;
    }

    private static ReplyKeyboard drawInlineMenu(
        List<Pair<String, String>> menuSource,
        int maxInRow)
        throws TelegramApiException {
        List<InlineKeyboardButton> buttons = menuSource
            .stream()
            .map(p -> {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setCallbackData(COMMAND_PREFIX + p.getKey());
                String label = p.getValue();
                button.setText(label);
                return button;
            })
            .collect(Collectors.toList());

        int size = buttons.size();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (int i = 0; i < size / maxInRow + (size % maxInRow > 0 ? 1 : 0); i++) {
            keyboard.add(buttons.subList(i * maxInRow, Math.min((i + 1) * maxInRow, size)));
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }

    public static void drawMenu(
        ChatState state,
        int maxInRow,
        String menuHeader,
        List<Pair<String, String>> menuSource,
        EntryBot sender
    ) throws TelegramApiException {
        ReplyKeyboard keyboard;
        if (menuSource.stream().anyMatch(p -> p.getKey().equals(SEND_CONTACT_COMMAND_NAME))) {
            keyboard = drawContactMenu(menuSource);
        } else {
            keyboard = drawInlineMenu(menuSource, maxInRow);
        }
        SendMessage msg = new SendMessage(state.getChatId().toString(), menuHeader);
        msg.setReplyMarkup(keyboard);
        msg.enableMarkdown(true);
        sender.execute(msg);
    }

}
