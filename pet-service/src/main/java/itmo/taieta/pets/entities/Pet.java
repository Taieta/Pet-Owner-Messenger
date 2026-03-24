package itmo.taieta.pets.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Color;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pet {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private String breed;
    private Color color;
    private Long ownerId;
    private List<Long> friendsId = new ArrayList<>();
}
