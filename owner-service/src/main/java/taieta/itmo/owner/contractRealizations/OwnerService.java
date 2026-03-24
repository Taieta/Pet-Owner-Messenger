package taieta.itmo.owner.contractRealizations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taieta.itmo.owner.contracts.IOwnerService;
import taieta.itmo.owner.entities.Owner;
import taieta.itmo.owner.exceptions.OwnerNotFoundException;
import taieta.itmo.owner.mappers.OwnerMapper;
import taieta.itmo.owner.repositories.OwnerRepository;

@Service
@RequiredArgsConstructor
public class OwnerService implements IOwnerService {
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    public Owner changeEntity(long id, Owner owner) throws OwnerNotFoundException {
        Owner existingOwner = ownerMapper.toEntity(ownerRepository.findById(id).orElseThrow(() -> new OwnerNotFoundException("Owner not found")));
        ownerMapper.updateEntity(owner, existingOwner);
        ownerRepository.save(ownerMapper.toModel(existingOwner));
        return existingOwner;
    }

    public void deleteOwner(long id) {
        ownerRepository.deleteById(id);
    }
}
