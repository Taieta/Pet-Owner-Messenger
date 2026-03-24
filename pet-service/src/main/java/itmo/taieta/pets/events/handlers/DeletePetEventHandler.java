package itmo.taieta.pets.events.handlers;

import dtos.PetDto;
import itmo.taieta.events.EventRequest;
import itmo.taieta.events.EventResponse;
import itmo.taieta.events.PetEvents;
import itmo.taieta.pets.contractRealization.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeletePetEventHandler implements PetEventHandler {
    private final PetService petService;

    @Override
    public boolean canHandle(String event) {
        return PetEvents.DELETE.name().equals(event);
    }

    @Override
    public void handle(EventRequest request, EventResponse response) {
        PetDto petDto = (PetDto) request.getData();
        petService.deletePet(petDto.getId());
        response.setData("Pet has been deleted");
    }
}
