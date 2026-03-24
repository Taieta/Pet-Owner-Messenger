package taieta.itmo.owner.mappers;

import org.springframework.stereotype.Component;
import taieta.itmo.owner.entities.Owner;
import taieta.itmo.owner.models.OwnerModel;

@Component
public class OwnerMapper {
    public Owner toEntity(OwnerModel ownerModel) {
        return new Owner(ownerModel.getId(), ownerModel.getName(), ownerModel.getBirthDate(), ownerModel.getUsername(), ownerModel.getPassword(), ownerModel.getRole());
    }

    public OwnerModel toModel(Owner owner) {
        return new OwnerModel(owner.getId(), owner.getName(), owner.getBirthDate(), owner.getUsername(), owner.getPassword(), owner.getRole());
    }

    public void updateEntity(Owner source, Owner target) {
        target.setName(source.getName());
        target.setBirthDate(source.getBirthDate());
        target.setPassword(source.getPassword());
    }
}
