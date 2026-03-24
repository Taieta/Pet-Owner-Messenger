package taieta.itmo.owner.contractRealizations;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import taieta.itmo.owner.entities.UserDetailsEntity;
import taieta.itmo.owner.mappers.OwnerMapper;
import taieta.itmo.owner.models.OwnerModel;
import taieta.itmo.owner.repositories.OwnerRepository;

@Service
@RequiredArgsConstructor
public class OwnerDetailsService implements UserDetailsService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        OwnerModel owner = ownerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new UserDetailsEntity(ownerMapper.toEntity(owner));
    }
}
