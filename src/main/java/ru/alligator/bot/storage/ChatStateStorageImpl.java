package ru.alligator.bot.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.alligator.bot.flow.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ChatStateStorageImpl implements ChatStateStorage {

    private final ObjectMapper objectMapper;
    private final HazelcastInstance hazelcastClient;
    private final IMap<Long, String> hazelcastMap;

    public ChatStateStorageImpl(HazelcastInstance hazelcastClient,
                                ObjectMapper objectMapper,
                                @Value("${hazelcast.map_name}") String mapName) {
        this.hazelcastClient = hazelcastClient;
        this.objectMapper = objectMapper;
        this.hazelcastMap = hazelcastClient.getMap(mapName);
    }

    @Override
    public boolean hasSate(Long chatId) {
        return hazelcastMap.containsKey(chatId);
    }

    private ChatState createDefaultState(Long chatId) {
        var state = new ChatState();
        state.setChatId(chatId);
        state.setCurrentStage(Stage.NOT_AUTHORIZED);
        return state;
    }
    @Override
    public ChatState getState(Long chatId) {
        String stateStr = hazelcastMap.get(chatId);
        if (stateStr == null) {
            return createDefaultState(chatId);
        }
        ChatState state;
        try {
            state = objectMapper.readValue(stateStr, ChatState.class);
        } catch (JsonProcessingException ex) {
            log.error(ex.getMessage(), ex);
            state = createDefaultState(chatId);
        }
        return state;
    }

    @SneakyThrows
    @Override
    public void putState(ChatState state) {
        hazelcastMap.put(state.getChatId(), objectMapper.writeValueAsString(state));
    }

    @SneakyThrows
    @Override
    public List<ChatState> getByEmployeeId(UUID employeeId) {
        return hazelcastMap.values().stream()
                .map(item -> {
                    try {
                        return objectMapper.readValue(item, ChatState.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).filter(state -> employeeId.equals(state.getEmployeeId()))
                .toList();
    }
}

