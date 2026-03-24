package itmo.taieta.pets.repositories;

import itmo.taieta.pets.models.PetModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<PetModel, Long> {
    @Query("select p.friends from PetModel p where p.id = :id")
    Page<PetModel> getAllFriends(@Param("id") long id, Pageable pageable);

    @Query("select p from PetModel p where p.ownerId = :ownerId")
    Page<PetModel> getOwnedPets(@Param("ownerId") long ownerId, Pageable pageable);
}
