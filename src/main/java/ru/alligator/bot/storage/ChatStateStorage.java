package ru.alligator.bot.storage;

import java.util.List;
import java.util.UUID;

public interface ChatStateStorage {
    boolean hasSate(Long chatId);
    ChatState getState(Long chatId);
    void putState(ChatState state);
    List<ChatState> getByEmployeeId(UUID employeeId);
}

