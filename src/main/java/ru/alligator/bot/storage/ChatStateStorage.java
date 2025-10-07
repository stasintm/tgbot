package ru.alligator.bot.storage;

public interface ChatStateStorage {
    boolean hasSate(Long chatId);
    ChatState getState(Long chatId);
    void putState(ChatState state);
}

