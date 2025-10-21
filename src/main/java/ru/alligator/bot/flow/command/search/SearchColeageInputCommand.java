package ru.alligator.bot.flow.command.search;

import static ru.alligator.bot.flow.command.CommandConstants.USER_INPUT_COMMAND_NAME;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import ru.alligator.bot.api.EmployeeFeignClient;
import ru.alligator.bot.api.TgbotBadRequestException;
import ru.alligator.bot.dto.EmployeeTo;
import ru.alligator.bot.flow.EntryBot;
import ru.alligator.bot.flow.TgUtils;
import ru.alligator.bot.flow.command.Command;
import ru.alligator.bot.flow.stage.Stage;
import ru.alligator.bot.storage.ChatState;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchColeageInputCommand implements Command {

    private final EmployeeFeignClient employeeFeignClient;

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
            if (!chatState.isApproved()) {
                throw new AccessDeniedException("Пользователь не авторизован!");
            }
            List<EmployeeTo> employees;
            try {
                employees = employeeFeignClient.findEmployees(searchStr);
            } catch (TgbotBadRequestException ex) {
                TgUtils.sendMessage(chatState.getChatId().toString(),
                    "Некорректная поисковая строка! Повторите еще раз",
                    sender);
                Command.enterStage(Stage.ENTER_SEARCH_STRING, chatState, sender);
                return;
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
                TgUtils.sendMessage(chatState.getChatId().toString(),
                    "Произошла ошибка при обработке запроса! Обратитесь к администратору!",
                    sender);
                Command.enterStage(Stage.AUTH_MAIN_MENU, chatState, sender);
                return;
            }
            if (employees.isEmpty()) {
                TgUtils.sendMessage(chatState.getChatId().toString(), "По вашему запросу ничего не найдено!",
                    sender);
            } else {
                employees.forEach(employee ->
                    TgUtils.sendMessage(chatState.getChatId().toString(),
                        String.format("%s %s %s\n%s", employee.getLastName(), employee.getFirstName(),
                            Optional.ofNullable(employee.getPatronymic()).orElse(""), employee.getPhone()),
                        sender));
            }
        }
        Command.enterStage(Stage.AUTH_MAIN_MENU, chatState, sender);
    }
}
