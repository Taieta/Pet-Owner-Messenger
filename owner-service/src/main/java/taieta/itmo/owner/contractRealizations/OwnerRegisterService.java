package taieta.itmo.owner.contractRealizations;

import lombok.RequiredArgsConstructor;
import models.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taieta.itmo.owner.contracts.IOwnerRegisterService;
import taieta.itmo.owner.entities.Owner;
import taieta.itmo.owner.mappers.OwnerMapper;
import taieta.itmo.owner.repositories.OwnerRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OwnerRegisterService implements IOwnerRegisterService {
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Owner register(String name, LocalDate birthday, String username, String password) {
        Owner newOwner = new Owner(null, name, birthday, username, passwordEncoder.encode(password), Role.USER);
        ownerRepository.save(ownerMapper.toModel(newOwner));
        return newOwner;
    }
}
