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
public class AddFriendPetEventHandler implements PetEventHandler {
    private final PetDtoMapper petDtoMapper;
    private final PetService petService;

    @Override
    public boolean canHandle(String event) {
        return PetEvents.ADD_FRIEND.name().equals(event);
    }

    @Override
    public void handle(EventRequest request, EventResponse response) {
        Object[] params = (Object[]) request.getData();
        Long petId = ((Number) params[0]).longValue();
        Long friendId = ((Number) params[1]).longValue();

        Pet pet = petService.makeFriend(petId, friendId);
        response.setData(petDtoMapper.toDTO(pet));
    }
}
