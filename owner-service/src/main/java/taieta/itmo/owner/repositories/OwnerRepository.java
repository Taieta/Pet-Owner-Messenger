package taieta.itmo.owner.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taieta.itmo.owner.models.OwnerModel;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<OwnerModel,Long>{
    Optional<OwnerModel> findByUsername(String username);
}
