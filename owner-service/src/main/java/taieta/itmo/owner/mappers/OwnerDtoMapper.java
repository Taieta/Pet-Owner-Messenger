package taieta.itmo.owner.mappers;

import dtos.OwnerDto;
import taieta.itmo.owner.entities.Owner;
import org.springframework.stereotype.Component;

@Component
public class OwnerDtoMapper {
    public OwnerDto toDTO(Owner owner) {
        return new OwnerDto(owner.getId(), owner.getName(), owner.getBirthDate(), owner.getUsername(), null, owner.getRole());
    }

    public Owner fromDTO(OwnerDto dto) {
        return new Owner(dto.getId(), dto.getName(), dto.getBirthDate(), dto.getUsername(), dto.getPassword(), dto.getRole());
    }
}