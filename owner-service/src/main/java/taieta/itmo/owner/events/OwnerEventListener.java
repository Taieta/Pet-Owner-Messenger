package taieta.itmo.owner.events;

import itmo.taieta.events.EventRequest;
import itmo.taieta.events.EventResponse;
import itmo.taieta.events.EventResponseStatus;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import taieta.itmo.owner.events.handlers.OwnerEventHandler;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OwnerEventListener {

    private final List<OwnerEventHandler> handlers;
    private final KafkaTemplate<String, EventResponse> kafkaTemplate;

    @Transactional
    @KafkaListener(topics = "${kafka.request.topic.owners}", groupId = "${kafka.group-id}")
    public void listenOwnerEvent(ConsumerRecord<String, EventRequest> record) {
        EventRequest request = record.value();
        EventResponse response = new EventResponse();
        response.setCorrelationId(request.getCorrelationId());

        try {
            for (OwnerEventHandler handler : handlers) {
                if (handler.canHandle(request.getOperation())) {
                    handler.handle(request, response);
                    response.setStatus(EventResponseStatus.SUCCESS.name());
                }
            }

            if (!Objects.equals(response.getStatus(), EventResponseStatus.SUCCESS.name())) {
                response.setStatus(EventResponseStatus.UNSUPPORTED.name());
            }
        } catch (Exception e) {
            response.setStatus(EventResponseStatus.FAILURE.name());
            response.setData(e.getMessage());
        }

        try {
            kafkaTemplate.send(record.topic(), response.getCorrelationId(), response);
        } catch (Exception e) {
            System.err.println("Failed to send responseMessage to Kafka: " + e.getMessage());
        }
    }
}
