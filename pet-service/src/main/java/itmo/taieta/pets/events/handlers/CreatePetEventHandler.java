package itmo.taieta.pets.events.handlers;

import dtos.PetDto;
import dtos.PetDto;
import itmo.taieta.events.EventRequest;
import itmo.taieta.events.EventResponse;
import itmo.taieta.events.PetEvents;
import itmo.taieta.pets.contractRealization.PetRegisterService;
import itmo.taieta.pets.entities.Pet;
import itmo.taieta.pets.mappers.PetDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatePetEventHandler implements PetEventHandler {
    private final PetDtoMapper petDtoMapper;
    private final PetRegisterService petRegisterService;

    @Override
    public boolean canHandle(String event) {
        return PetEvents.CREATE.name().equals(event);
    }

    @Override
    public void handle(EventRequest request, EventResponse response) {
        PetDto petDto = (PetDto) request.getData();
        Pet newPet = petRegisterService.register(petDto.getName(), petDto.getBirthDate(), petDto.getBreed(), petDto.getColor(), petDto.getOwnerId());
        response.setData(petDtoMapper.toDTO(newPet));
    }
}
