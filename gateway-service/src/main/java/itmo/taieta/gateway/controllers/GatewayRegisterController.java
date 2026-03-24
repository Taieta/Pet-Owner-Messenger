package itmo.taieta.gateway.controllers;

import dtos.OwnerDto;
import dtos.PetDto;
import itmo.taieta.events.*;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/register")
public class GatewayRegisterController {

    private final ReplyingKafkaTemplate<String, EventRequest, EventResponse> kafkaTemplate;
    private final String requestTopic;
    private final String replyTopic;
    private final long replyTimeout;

    public GatewayRegisterController(
            ReplyingKafkaTemplate<String, EventRequest, EventResponse> kafkaTemplate,
            @Value("${kafka.request.topic.owners}") String requestTopic,
            @Value("${kafka.reply.topic.owners}") String replyTopic,
            @Value("${kafka.reply.timeout:5000}") long replyTimeout) {

        this.kafkaTemplate = kafkaTemplate;
        this.requestTopic = requestTopic;
        this.replyTopic = replyTopic;
        this.replyTimeout = replyTimeout;
    }

    @PostMapping("/owner")
    public ResponseEntity<OwnerDto> createOwner(@RequestBody OwnerDto ownerDTO) {
        EventRequest requestMessage = new EventRequest(OwnerEvents.CREATE.name(), ownerDTO);

        EventResponse responseMessage = sendAndReceive(requestMessage);

        return handleCreateResponseOwner(responseMessage);
    }

    @PostMapping("/pet")
    public ResponseEntity<PetDto> createPet(@RequestBody PetDto dto) {
        EventRequest requestMessage = new EventRequest(PetEvents.CREATE.name(), dto);
        return handleCreateResponsePet(sendAndReceive(requestMessage));
    }

    private EventResponse sendAndReceive(EventRequest requestMessage) {
        EventResponse response = null;
        try {
            ProducerRecord<String, EventRequest> record = new ProducerRecord<>(requestTopic, requestMessage);

            response = kafkaTemplate.sendAndReceive(record)
                    .get(replyTimeout, TimeUnit.MILLISECONDS)
                    .getPayload();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't send a message to the topic: " + e.getMessage());
        }
        return response;
    }


    private ResponseEntity<OwnerDto> handleCreateResponseOwner(EventResponse responseMessage) {
        if (responseMessage == null) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
        }
        if (EventResponseStatus.SUCCESS.name().equals(responseMessage.getStatus())) {
            return ResponseEntity.status(HttpStatus.CREATED).body((OwnerDto) responseMessage.getData());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private ResponseEntity<PetDto> handleCreateResponsePet(EventResponse responseMessage) {
        if (responseMessage == null) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
        }
        if (EventResponseStatus.SUCCESS.name().equals(responseMessage.getStatus())) {
            return ResponseEntity.status(HttpStatus.CREATED).body((PetDto) responseMessage.getData());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
