package itmo.taieta.pets.events.handlers;

import dtos.PetDto;
import itmo.taieta.events.EventRequest;
import itmo.taieta.events.EventResponse;
import itmo.taieta.events.OwnerEvents;
import itmo.taieta.pets.contractRealization.PetService;
import itmo.taieta.pets.entities.Pet;
import itmo.taieta.pets.mappers.PetDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetPetsEventHandler implements PetEventHandler {
    private final PetDtoMapper petDtoMapper;
    private final PetService petService;

    @Override
    public boolean canHandle(String event) {
        return OwnerEvents.GET_PETS.name().equals(event);
    }

    @Override
    public void handle(EventRequest requestMessage, EventResponse responseMessage) {
        Object[] params = (Object[]) requestMessage.getData();
        Long ownerId = ((Number) params[0]).longValue();
        int page = (int) params[1];
        int size = (int) params[2];

        Pageable pageable = PageRequest.of(page, size);
        Page<Pet> petsPage = petService.getPetsByOwnerId(ownerId, pageable);

        List<PetDto> petsList = petsPage.getContent()
                .stream()
                .map(petDtoMapper::toDTO)
                .collect(Collectors.toList());

        responseMessage.setData(petsList);
    }
}
