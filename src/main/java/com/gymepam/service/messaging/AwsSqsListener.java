package com.gymepam.service.messaging;

import com.gymepam.config.QueuesConfig;
import com.gymepam.service.training.TrainingInMemoryStorage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

@Slf4j
@Component
@ConditionalOnProperty(name = "microservice.connection", havingValue = "aws")
@AllArgsConstructor
public class AwsSqsListener{
    private TrainingInMemoryStorage trainingInMemoryStorage;
    private final SqsClient sqsClient;
    private QueuesConfig queuesConfig;


    @Scheduled(fixedRate = 5000)
    public void pollMessages() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queuesConfig.getResponses())
                .messageAttributeNames("All")
                .maxNumberOfMessages(10)
                .build();

        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();

        for (Message message : messages) {
            try {
                String processId = message.messageAttributes().get("processId").stringValue();
                if (processId == null || processId.isEmpty()) {
                    log.info("No response");
                    continue;
                }

                String response = message.body();
                if (response == null) {
                    log.info("No response");
                    continue;
                }

                trainingInMemoryStorage.setTrainingResponse(processId, response);

                // Delete the message after processing it
                DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                        .queueUrl(queuesConfig.getResponses())
                        .receiptHandle(message.receiptHandle())
                        .build();
                sqsClient.deleteMessage(deleteMessageRequest);

            } catch (Exception ex) {
                log.error("Error processing message: {}", ex.getMessage());
            }
        }
    }
}
