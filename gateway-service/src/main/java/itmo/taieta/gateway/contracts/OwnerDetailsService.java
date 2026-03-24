package itmo.taieta.gateway.contracts;

import itmo.taieta.gateway.entities.UserDetailsEntity;
import itmo.taieta.gateway.mappers.OwnerMapper;
import itmo.taieta.gateway.models.OwnerModel;
import itmo.taieta.gateway.repositories.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
