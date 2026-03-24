package taieta.itmo.owner.events.handlers;

import dtos.OwnerDto;
import itmo.taieta.events.EventRequest;
import itmo.taieta.events.EventResponse;
import itmo.taieta.events.OwnerEvents;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import taieta.itmo.owner.contractRealizations.OwnerService;
import taieta.itmo.owner.entities.Owner;
import taieta.itmo.owner.mappers.OwnerDtoMapper;

@Component
@RequiredArgsConstructor
public class UpdateOwnerEventHandler implements OwnerEventHandler {
    private final OwnerService ownerService;
    private final OwnerDtoMapper ownerDtoMapper;

    @Override
    public boolean canHandle(String event) {
        return OwnerEvents.UPDATE.name().equals(event);
    }

    @Override
    public void handle(EventRequest request,  EventResponse response) {
        OwnerDto ownerDto = (OwnerDto) request.getData();
        Owner owner = ownerDtoMapper.fromDTO(ownerDto);
        Owner updatedOwner = ownerService.changeEntity(owner.getId(), owner);
        response.setData(ownerDtoMapper.toDTO(updatedOwner));
    }
}
