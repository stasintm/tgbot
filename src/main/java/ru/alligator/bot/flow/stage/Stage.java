package ru.alligator.bot.flow.stage;

import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import ru.alligator.bot.flow.command.CommandConstants;

import java.util.List;

public enum Stage {
    NOT_AUTHORIZED(
            "\uD83D\uDCF1 Вас приветствует бот компании *Аллигатор*!",
            InputType.NONE,
            List.of(Pair.of(CommandConstants.REGISTER_COMMAND_NAME, CommandConstants.REGISTER_COMMAND_TITLE),
            Pair.of(CommandConstants.HELP_COMMAND_NAME, CommandConstants.HELP_COMMAND_TITLE))
    ),
    REG_NUMBER(
            "Для доступа к полной версии нужен ваш контакт.\nНажмите кнопку \uD83D\uDCF1 *Мой контакт* для отправки",
            InputType.CONTACT,
            List.of(Pair.of(CommandConstants.SEND_CONTACT_COMMAND_NAME, CommandConstants.SEND_CONTACT_COMMAND_TITLE))
    ),
    AUTH_MAIN_MENU(
            "\uD83D\uDCF1 Вас приветствует бот компании *Аллигатор*!",
            InputType.NONE,
            List.of(Pair.of(CommandConstants.SEARCH_COMMAND_NAME, CommandConstants.SEARCH_COMMAND_TITLE),
                Pair.of(CommandConstants.VIEW_VACATION_COMMAND_NAME, CommandConstants.VIEW_VACATION_COMMAND_TITLE),
                Pair.of(CommandConstants.REQUEST_VACATION_COMMAND_NAME, CommandConstants.REQUEST_VACATION_COMMAND_TITLE),
                Pair.of(CommandConstants.HELP_COMMAND_NAME, CommandConstants.HELP_COMMAND_TITLE))
    ),

    ENTER_SEARCH_STRING(
            "Введите поисковую строку",
            InputType.TEXT,
            List.of(Pair.of(CommandConstants.ABORT_COMMAND_NAME, CommandConstants.ABORT_COMMAND_TITLE))
    ),

    ENTER_START_DATE(
        "Введите дату начала отпуска (формат DD.MM.YYYY, дата должна быть в будущем минимум на 3 дня)",
        InputType.TEXT,
        List.of(Pair.of(CommandConstants.ABORT_COMMAND_NAME, CommandConstants.ABORT_COMMAND_TITLE))
    ),

    ENTER_END_DATE(
        "Введите дату последнего дня отпуска (формат DD.MM.YYYY, дата должна быть в будущем минимум на 3 дня)",
        InputType.TEXT,
        List.of(Pair.of(CommandConstants.ABORT_COMMAND_NAME, CommandConstants.ABORT_COMMAND_TITLE))
    ),

    VACATION_CONFIRMATION(
        "Подтверждение создания отпуска",
        InputType.NONE,
        List.of(Pair.of(CommandConstants.CONFIRM_VACATION_COMMAND_NAME, CommandConstants.CONFIRM_VACATION_COMMAND_TITLE),
            Pair.of(CommandConstants.ABORT_COMMAND_NAME, CommandConstants.ABORT_COMMAND_TITLE))
    );

    @Getter
    private final String header;
    @Getter
    private final List<Pair<String, String>> buttons;
    @Getter
    private int maxButtonsInRow = 1;
    private final InputType waitInputType;

    Stage(String header, InputType waitInputType, List<Pair<String, String>> buttons) {
        this.header = header;
        this.buttons = buttons;
        this.waitInputType = waitInputType;
    }

    Stage(String header, InputType waitInputType, int maxButtonsInRow, List<Pair<String, String>> buttons) {
        this.header = header;
        this.buttons = buttons;
        this.maxButtonsInRow = maxButtonsInRow;
        this.waitInputType = waitInputType;
    }

    public InputType waitInputType() {
        return waitInputType;
    }
}
