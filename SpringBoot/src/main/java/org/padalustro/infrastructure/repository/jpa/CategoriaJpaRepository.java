package org.padalustro.infrastructure.repository.jpa;

import java.util.List;

import org.padalustro.domain.entities.categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoriaJpaRepository extends JpaRepository<categoria, Long> {
    // Spring Data genera automàticament findAll, findById, deleteById, existsById, etc.
    List<categoria> findByNameIgnoreCase(String name);
}
