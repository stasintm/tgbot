package ru.alligator.bot.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.alligator.bot.dto.VacationTo;
import ru.alligator.bot.flow.EntryBot;
import ru.alligator.bot.flow.TgUtils;
import ru.alligator.bot.storage.ChatStateStorage;

@Component
@RequiredArgsConstructor
public class VacationNotificationListener {

    private final ObjectMapper objectMapper;

    private final ChatStateStorage storage;

    private final EntryBot bot;

    @SneakyThrows
    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(ConsumerRecord<String, String> record) {
        var to = objectMapper.readValue(record.value(), VacationTo.class);
        storage.getByEmployeeId(to.getEmployeeId()).forEach(state -> {
            TgUtils.sendMessage(state.getChatId().toString(),
                    String.format("Ваш отпуск с %s по %s согласован!",
                            TgUtils.DATE_TIME_FORMATTER.format(to.getStartDate()),
                            TgUtils.DATE_TIME_FORMATTER.format(to.getEndDate())), bot);
        });

    }
}
