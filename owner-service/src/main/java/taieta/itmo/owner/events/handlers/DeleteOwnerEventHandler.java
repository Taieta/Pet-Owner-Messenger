package taieta.itmo.owner.events.handlers;

import dtos.OwnerDto;
import itmo.taieta.events.EventRequest;
import itmo.taieta.events.EventResponse;
import itmo.taieta.events.OwnerEvents;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import taieta.itmo.owner.contractRealizations.OwnerService;

@Component
@RequiredArgsConstructor
public class DeleteOwnerEventHandler implements OwnerEventHandler {
    private final OwnerService ownerService;

    @Override
    public boolean canHandle(String event) {
        return OwnerEvents.DELETE.name().equals(event);
    }

    @Override
    public void handle(EventRequest request,  EventResponse response) {
        OwnerDto ownerDto = (OwnerDto) request.getData();
        ownerService.deleteOwner(ownerDto.getId());
        response.setData("Owner deleted successfully");
    }
}
