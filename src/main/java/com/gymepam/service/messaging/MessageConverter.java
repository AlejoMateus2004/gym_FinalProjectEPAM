package com.gymepam.service.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.jms.JMSException;
import jakarta.jms.Message;

public interface MessageConverter {
    <T> T convertMessageToObject(Message message, Class<T> object) throws JsonProcessingException, JMSException;
}
