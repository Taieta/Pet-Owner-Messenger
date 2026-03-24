package itmo.taieta.gateway.controllers;

import dtos.OwnerDto;
import dtos.PetDto;
import itmo.taieta.events.EventRequest;
import itmo.taieta.events.EventResponse;
import itmo.taieta.events.EventResponseStatus;
import itmo.taieta.events.OwnerEvents;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/owners")
public class GatewayOwnerController {

    private final ReplyingKafkaTemplate<String, EventRequest, EventResponse> kafkaTemplate;
    private final String requestTopic;
    private final long replyTimeout;

    public GatewayOwnerController(
            ReplyingKafkaTemplate<String, EventRequest, EventResponse> kafkaTemplate,
            @Value("${kafka.request.topic.owners}") String requestTopic,
            @Value("${kafka.reply.timeout:5000}") long replyTimeout) {

        this.kafkaTemplate = kafkaTemplate;
        this.requestTopic = requestTopic;
        this.replyTimeout = replyTimeout;
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.ownerReferenceId")
    @PutMapping("/{id}")
    public ResponseEntity<OwnerDto> updateOwner(
            @PathVariable Long id,
            @RequestBody OwnerDto ownerDTO
    ) {
        Object[] params = {id, ownerDTO};
        EventRequest requestMessage = new EventRequest(OwnerEvents.UPDATE.name(), params);

        EventResponse responseMessage = sendAndReceive(requestMessage);

        return handleResponse(responseMessage);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.ownerReferenceId")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        EventRequest requestMessage = new EventRequest(OwnerEvents.DELETE.name(), id);

        EventResponse responseMessage = sendAndReceive(requestMessage);

        return handleDeleteResponse(responseMessage);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{ownerId}/pets")
    public ResponseEntity<List<PetDto>> getPetsByOwner(
            @PathVariable Long ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Object[] params = {ownerId, page, size};
        EventRequest requestMessage = new EventRequest(OwnerEvents.GET_PETS.name(), params);

        EventResponse responseMessage = sendAndReceive(requestMessage);

        return handlePetListResponse(responseMessage);
    }

    private EventResponse sendAndReceive(EventRequest requestMessage) {
        Message<EventRequest> message = MessageBuilder
                .withPayload(requestMessage)
                .setHeader(KafkaHeaders.TOPIC, requestTopic)
                .build();

        EventResponse response = null;

        try {
            response = (EventResponse) kafkaTemplate.sendAndReceive(message)
                    .get(replyTimeout, TimeUnit.MILLISECONDS)
                    .getPayload();
        } catch (Exception e) {
            System.out.println("Couldn't send a message to the topic");
        }

        return response;
    }

    private ResponseEntity<OwnerDto> handleResponse(EventResponse responseMessage) {
        if (responseMessage == null) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
        }
        if (EventResponseStatus.SUCCESS.name().equals(responseMessage.getStatus())) {
            return ResponseEntity.ok((OwnerDto) responseMessage.getData());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private ResponseEntity<Void> handleDeleteResponse(EventResponse responseMessage) {
        if (responseMessage == null) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
        }
        if (EventResponseStatus.SUCCESS.name().equals(responseMessage.getStatus())) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private ResponseEntity<List<PetDto>> handlePetListResponse(EventResponse responseMessage) {
        if (responseMessage == null) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
        }
        if (EventResponseStatus.SUCCESS.name().equals(responseMessage.getStatus())) {
            return ResponseEntity.ok((List<PetDto>) responseMessage.getData());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

