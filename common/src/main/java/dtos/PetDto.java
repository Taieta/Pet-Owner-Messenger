package dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
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
public class PetDto {
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Birth date is mandatory")
    @Past(message = "Birth date must be in the past")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;

    @NotBlank(message = "Breed is mandatory")
    private String breed;

    @NotNull(message = "Color is mandatory")
    private Color color;

    @NotNull(message = "Owner is mandatory")
    private Long ownerId;

    private List<Long> friendsId = new ArrayList<>();
}
