package ru.alligator.bot.api;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
public class EmployeeErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaulDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (methodKey.contains("findEmployees") &&  response.body() != null) {
            var body = getBody(response.body());
            if (body.contains("Internal Server Error")) {
                return new TgbotBadRequestException();
            }
        }
        return defaulDecoder.decode(methodKey, response);
    }

    @SneakyThrows
    private String getBody(Response.Body body) {
        return new String(body.asInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}
