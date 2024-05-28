package com.gymepam.service.messaging;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.AllArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class Producer {

    private JmsTemplate jmsTemplate;

    public String sendMessage(String destination, Object messageContent){
        String processId = UUID.randomUUID().toString();
        jmsTemplate.convertAndSend(destination, messageContent, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setStringProperty("processId", processId);
                return message;
            }
        });
        return processId;
    }
}
