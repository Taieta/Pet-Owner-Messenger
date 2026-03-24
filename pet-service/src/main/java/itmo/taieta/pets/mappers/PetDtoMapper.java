package itmo.taieta.pets.mappers;

import dtos.PetDto;
import itmo.taieta.pets.entities.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetDtoMapper {
    public PetDto toDTO(Pet pet) {
        return new PetDto(pet.getId(), pet.getName(), pet.getBirthDate(), pet.getBreed(), pet.getColor(), pet.getOwnerId(), pet.getFriendsId());
    }

    public Pet fromDTO(PetDto dto) {
        return new Pet(dto.getId(), dto.getName(), dto.getBirthDate(), dto.getBreed(), dto.getColor(), dto.getOwnerId(), dto.getFriendsId());
    }
}
