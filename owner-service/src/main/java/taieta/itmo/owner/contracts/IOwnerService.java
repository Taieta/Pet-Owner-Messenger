package taieta.itmo.owner.contracts;

import taieta.itmo.owner.entities.Owner;

public interface IOwnerService {
    Owner changeEntity(long id, Owner owner);
    void deleteOwner(long id);
}
