package taieta.itmo.owner.events.handlers;

import dtos.OwnerDto;
import itmo.taieta.events.EventRequest;
import itmo.taieta.events.EventResponse;
import itmo.taieta.events.OwnerEvents;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taieta.itmo.owner.contracts.IOwnerRegisterService;
import taieta.itmo.owner.entities.Owner;
import taieta.itmo.owner.mappers.OwnerDtoMapper;

@Component
@RequiredArgsConstructor
public class CreateOwnerEventHandler implements OwnerEventHandler {
    private final IOwnerRegisterService ownerRegisterService;
    private final OwnerDtoMapper ownerDtoMapper;

    @Override
    public boolean canHandle(String event) {
        return OwnerEvents.CREATE.name().equals(event);
    }

    @Override
    public void handle(EventRequest request, EventResponse response) {
        OwnerDto ownerDto = (OwnerDto) request.getData();
        Owner newOwner = ownerRegisterService.register(ownerDto.getName(), ownerDto.getBirthDate(), ownerDto.getUsername(), ownerDto.getPassword());
        response.setData(ownerDtoMapper.toDTO(newOwner));
    }
}
