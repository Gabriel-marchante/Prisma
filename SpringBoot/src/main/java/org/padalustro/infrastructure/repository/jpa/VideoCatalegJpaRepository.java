package org.padalustro.infrastructure.repository.jpa;

import org.padalustro.domain.entities.video_cataleg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoCatalegJpaRepository extends JpaRepository<video_cataleg, Long> {
    // Spring Data genera automàticament findAll, findById, deleteById, existsById, etc.
}
