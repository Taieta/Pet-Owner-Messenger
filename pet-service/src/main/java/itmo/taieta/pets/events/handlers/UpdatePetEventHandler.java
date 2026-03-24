package itmo.taieta.pets.events.handlers;

import dtos.PetDto;
import itmo.taieta.events.EventRequest;
import itmo.taieta.events.EventResponse;
import itmo.taieta.events.PetEvents;
import itmo.taieta.pets.contractRealization.PetService;
import itmo.taieta.pets.entities.Pet;
import itmo.taieta.pets.mappers.PetDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdatePetEventHandler implements PetEventHandler {
    private final PetDtoMapper petDtoMapper;
    private final PetService petService;

    @Override
    public boolean canHandle(String event) {
        return PetEvents.UPDATE.name().equals(event);
    }

    @Override
    public void handle(EventRequest request,  EventResponse response) {
        PetDto petDto = (PetDto) request.getData();
        Pet pet = petDtoMapper.fromDTO(petDto);
        Pet updatedPet = petService.changeEntity(pet.getId(), pet);
        response.setData(petDtoMapper.toDTO(updatedPet));
    }
}
