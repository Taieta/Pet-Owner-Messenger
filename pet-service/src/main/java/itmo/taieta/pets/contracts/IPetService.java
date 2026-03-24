package itmo.taieta.pets.contracts;

import itmo.taieta.pets.entities.Pet;
import itmo.taieta.pets.exceptions.PetNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPetService {
    Pet changeEntity(long id, Pet pet) throws PetNotFoundException;
    Pet makeFriend(long friendId, long petId);
    Pet breakFriend(long friendId, long petId);
    Page<Pet> getFriends(long id, Pageable pageable);
    Page<Pet> getPetsByOwnerId(long id, Pageable pageable);
    void deletePet(long id);
}
