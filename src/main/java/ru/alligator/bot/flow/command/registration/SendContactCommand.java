package ru.alligator.bot.flow.command.registration;

import static ru.alligator.bot.flow.command.CommandConstants.SEND_CONTACT_COMMAND_NAME;
import static ru.alligator.bot.flow.command.CommandConstants.SEND_CONTACT_COMMAND_TITLE;

import org.springframework.stereotype.Component;
import ru.alligator.bot.flow.EntryBot;
import ru.alligator.bot.flow.command.Command;
import ru.alligator.bot.storage.ChatState;

@Component
public class SendContactCommand implements Command {

    public static final String NAME = "sendcontact";

    public String getName() {
        return SEND_CONTACT_COMMAND_NAME;
    }

    @Override
    public String getLabel() {
        return SEND_CONTACT_COMMAND_TITLE;
    }

    public void acceptMessage(String entries, ChatState chatState, EntryBot sender) {
    }
}
