package com.gymepam.service.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AwsSqsProducer implements Producer{

    @Autowired
    private SqsClient sqsClient;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public String sendMessage(String queueUrl, Object messageContent) throws JsonProcessingException {
        String processId = UUID.randomUUID().toString();
        String jsonMessage = objectMapper.writeValueAsString(messageContent);
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("processId", MessageAttributeValue.builder()
                .stringValue(processId)
                .dataType("String")
                .build());

        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(jsonMessage)
                .messageAttributes(messageAttributes)
                .messageGroupId("default")
                .messageDeduplicationId(UUID.randomUUID().toString())
                .build();

        sqsClient.sendMessage(sendMsgRequest);
        return processId;
    }
}
