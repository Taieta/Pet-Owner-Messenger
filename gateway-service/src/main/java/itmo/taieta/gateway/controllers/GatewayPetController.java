package itmo.taieta.gateway.controllers;

import dtos.PetDto;
import itmo.taieta.events.EventRequest;
import itmo.taieta.events.EventResponse;
import itmo.taieta.events.EventResponseStatus;
import itmo.taieta.events.PetEvents;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/pets")
public class GatewayPetController {

    private final ReplyingKafkaTemplate<String, EventRequest, EventResponse> kafkaTemplate;
    private final String requestTopic;
    private final long replyTimeout;

    public GatewayPetController(
            ReplyingKafkaTemplate<String, EventRequest, EventResponse> kafkaTemplate,
            @Value("${kafka.request.topic.owners}") String requestTopic,
            @Value("${kafka.reply.timeout:5000}") long replyTimeout) {

        this.kafkaTemplate = kafkaTemplate;
        this.requestTopic = requestTopic;
        this.replyTimeout = replyTimeout;
    }

    @PreAuthorize("hasRole('ADMIN') or #dto.ownerId == authentication.principal.ownerReferenceId")
    @PutMapping("/{id}")
    public ResponseEntity<PetDto> updatePet(@PathVariable Long id, @RequestBody PetDto dto) {
        Object[] params = {id, dto};
        EventRequest requestMessage = new EventRequest(PetEvents.UPDATE.name(), params);
        return handleResponse(sendAndReceive(requestMessage));
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.ownerReferenceId")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        EventRequest requestMessage = new EventRequest("DELETE_PET", id);
        return handleDeleteResponse(sendAndReceive(requestMessage));
    }

    @PreAuthorize("@ownerService.isOwner(#petId, authentication.principal.ownerReferenceId)")
    @PostMapping("/{petId}/friends/{friendId}")
    public ResponseEntity<PetDto> addFriend(@PathVariable Long petId, @PathVariable Long friendId) {
        Object[] params = {petId, friendId};
        EventRequest requestMessage = new EventRequest(PetEvents.ADD_FRIEND.name(), params);
        return handleResponse(sendAndReceive(requestMessage));
    }

    @PreAuthorize("@ownerService.isOwner(#petId, authentication.principal.ownerReferenceId)")
    @DeleteMapping("/{petId}/friends/{friendId}")
    public ResponseEntity<PetDto> removeFriend(@PathVariable Long petId, @PathVariable Long friendId) {
        Object[] params = {petId, friendId};
        EventRequest requestMessage = new EventRequest(PetEvents.BREAK_FRIEND.name(), params);
        return handleResponse(sendAndReceive(requestMessage));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{petId}/friends")
    public ResponseEntity<List<PetDto>> getFriends(
            @PathVariable Long petId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Object[] params = {petId, page, size};
        EventRequest requestMessage = new EventRequest(PetEvents.GET_FRIENDS.name(), params);
        return handleListResponse(sendAndReceive(requestMessage));
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

    private ResponseEntity<PetDto> handleResponse(EventResponse responseMessage) {
        if (responseMessage == null) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
        }
        if (EventResponseStatus.SUCCESS.name().equals(responseMessage.getStatus())) {
            return ResponseEntity.ok((PetDto) responseMessage.getData());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private ResponseEntity<List<PetDto>> handleListResponse(EventResponse responseMessage) {
        if (responseMessage == null) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
        }
        if (EventResponseStatus.SUCCESS.name().equals(responseMessage.getStatus())) {
            return ResponseEntity.ok((List<PetDto>) responseMessage.getData());
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
}
