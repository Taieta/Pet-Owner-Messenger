package itmo.taieta.pets.mappers;

import itmo.taieta.pets.entities.Pet;
import itmo.taieta.pets.models.PetModel;
import itmo.taieta.pets.repositories.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PetMapper {
    private final PetRepository petRepository;

    public Pet toEntity(PetModel pet) {
        List<PetModel> pets = pet.getFriends();
        List<Long> ids = new ArrayList<>();
        for (PetModel newPet : pets) {
            ids.add(newPet.getId());
        }

        return new Pet(pet.getId(), pet.getName(), pet.getBirthDate(), pet.getBreed(), pet.getColor(), pet.getOwnerId(), ids);
    }

    public PetModel toModel(Pet pet) {
        List<Long> petsIds = pet.getFriendsId();
        List<PetModel> pets = new ArrayList<>();
        for (Long petId : petsIds) {
            pets.add(petRepository.findById(petId).get());
        }

        return new PetModel(pet.getId(), pet.getName(), pet.getBirthDate(), pet.getBreed(), pet.getColor(), pet.getOwnerId(), pets);
    }

    public void updateEntity(Pet source, Pet target) {
        target.setName(source.getName());
        target.setBirthDate(source.getBirthDate());
        target.setBreed(source.getBreed());
        target.setColor(source.getColor());
        target.setOwnerId(source.getOwnerId());
        target.setFriendsId(source.getFriendsId());
    }
}
