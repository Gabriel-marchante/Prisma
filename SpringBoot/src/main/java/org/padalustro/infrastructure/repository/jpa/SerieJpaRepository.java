package org.padalustro.infrastructure.repository.jpa;

import java.util.List;

import org.padalustro.domain.entities.serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SerieJpaRepository extends JpaRepository<serie, Long> {
    // Spring Data genera automàticament findAll, findById, deleteById, existsById, etc.
    List<serie> findByNameIgnoreCase(String name);
}
