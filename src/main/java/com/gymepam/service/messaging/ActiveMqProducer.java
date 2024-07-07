package com.gymepam.service.messaging;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

import java.util.UUID;

public class ActiveMqProducer implements Producer{
    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
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
