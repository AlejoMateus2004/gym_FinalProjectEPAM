package com.gymepam.service.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.AllArgsConstructor;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageConverterImpl implements MessageConverter {
    private ObjectMapper objectMapper;
    @Override
    public <T> T convertMessageToObject(Message message, Class<T> object) throws JsonProcessingException, JMSException {
        String text = ((ActiveMQTextMessage) message).getText();
        return objectMapper.readValue(text, object);
    }
}
