package itmo.taieta.pets.contracts;

import itmo.taieta.pets.entities.Pet;
import models.Color;

import java.time.LocalDate;

public interface IPetRegisterService {
    Pet register(String name, LocalDate birthday, String breed, Color color, Long ownerId);
}
