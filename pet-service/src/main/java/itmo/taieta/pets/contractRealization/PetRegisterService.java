package itmo.taieta.pets.contractRealization;

import itmo.taieta.pets.contracts.IPetRegisterService;
import itmo.taieta.pets.entities.Pet;
import itmo.taieta.pets.mappers.PetMapper;
import itmo.taieta.pets.repositories.PetRepository;
import lombok.RequiredArgsConstructor;
import models.Color;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PetRegisterService implements IPetRegisterService {
    private final PetRepository petRepository;
    private final PetMapper petMapper;

    public Pet register(String name, LocalDate birthday, String breed, Color color, Long ownerId) {
        Pet newPet = new Pet(null, name, birthday, breed, color, ownerId, new ArrayList<>());
        petRepository.save(petMapper.toModel(newPet));
        return newPet;
    }
}
