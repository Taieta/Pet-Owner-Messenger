package itmo.taieta.gateway.repositories;

import itmo.taieta.gateway.models.OwnerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<OwnerModel,Long>{
    Optional<OwnerModel> findByUsername(String username);
}
