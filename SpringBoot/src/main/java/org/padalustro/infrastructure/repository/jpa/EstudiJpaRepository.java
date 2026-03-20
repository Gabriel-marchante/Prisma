package org.padalustro.infrastructure.repository.jpa;

import java.util.List;

import org.padalustro.domain.entities.estudi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EstudiJpaRepository extends JpaRepository<estudi, Long> {
    // Spring Data genera automàticament findAll, findById, deleteById, existsById, etc.
    List<estudi> findByNameIgnoreCase(String name);
}
