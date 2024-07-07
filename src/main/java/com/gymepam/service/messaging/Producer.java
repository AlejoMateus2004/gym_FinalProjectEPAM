package com.gymepam.service.messaging;


import com.fasterxml.jackson.core.JsonProcessingException;

public interface Producer {
    String sendMessage(String destination, Object messageContent) throws JsonProcessingException;
}
