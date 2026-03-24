package itmo.taieta.pets.contractRealization;

import itmo.taieta.pets.contracts.IPetService;
import itmo.taieta.pets.entities.Pet;
import itmo.taieta.pets.exceptions.PetNotFoundException;
import itmo.taieta.pets.mappers.PetMapper;
import itmo.taieta.pets.repositories.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetService implements IPetService {
    private final PetRepository petRepository;
    private final PetMapper petMapper;

    public Pet changeEntity(long id, Pet pet) throws PetNotFoundException {
        Pet existingPet = petMapper.toEntity(petRepository.findById(id).orElseThrow(() -> new PetNotFoundException("Pet not found")));
        petMapper.updateEntity(pet, existingPet);
        petRepository.save(petMapper.toModel(existingPet));
        return existingPet;
    }

    @Transactional
    public Pet makeFriend(long friendId, long petId) throws PetNotFoundException {
        Pet pet = petMapper.toEntity(petRepository.findById(petId).orElseThrow(() -> new PetNotFoundException("Pet not found")));
        Pet friend = petMapper.toEntity(petRepository.findById(friendId).orElseThrow(() -> new PetNotFoundException("Friend not found")));

        pet.getFriendsId().add(friendId);
        friend.getFriendsId().add(petId);

        petRepository.save(petMapper.toModel(pet));
        petRepository.save(petMapper.toModel(friend));

        return pet;
    }

    @Transactional
    public Pet breakFriend(long friendId, long petId) throws PetNotFoundException {
        Pet pet = petMapper.toEntity(petRepository.findById(petId).orElseThrow(() -> new PetNotFoundException("Pet not found")));
        Pet friend = petMapper.toEntity(petRepository.findById(friendId).orElseThrow(() -> new PetNotFoundException("Friend not found")));

        pet.getFriendsId().remove(friendId);
        friend.getFriendsId().remove(petId);

        petRepository.save(petMapper.toModel(pet));
        petRepository.save(petMapper.toModel(friend));

        return pet;
    }

    @Transactional
    public Page<Pet> getFriends(long id, Pageable pageable) {
        return petRepository.getAllFriends(id, pageable).map(petMapper::toEntity);
    }

    @Transactional
    public Page<Pet> getPetsByOwnerId(long ownerId, Pageable pageable) {
        return petRepository.getOwnedPets(ownerId, pageable).map(petMapper::toEntity);
    }

    public void deletePet(long id) {
        petRepository.deleteById(id);
    }

    public boolean isOwner(Long ownerId, long petId) throws PetNotFoundException {
        Pet pet = petMapper.toEntity(petRepository.findById(petId).orElseThrow(() -> new PetNotFoundException("Pet not found")));
        return pet.getOwnerId().equals(ownerId);
    }
}
