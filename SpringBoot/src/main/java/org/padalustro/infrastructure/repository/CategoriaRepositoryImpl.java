package org.padalustro.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.padalustro.domain.entities.categoria;
import org.padalustro.domain.repository.CategoriaRepository;
import org.padalustro.infrastructure.DTO.CategoriaDTO;
import org.padalustro.infrastructure.repository.jpa.CategoriaJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoriaRepositoryImpl implements CategoriaRepository {

    private final CategoriaJpaRepository jpaRepository;

    public CategoriaRepositoryImpl(CategoriaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<CategoriaDTO> findAllDTO() {
        return jpaRepository.findAll()
                .stream()
                .map(CategoriaDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void save(CategoriaDTO categoriaDTO) {
        jpaRepository.save(categoriaDTO.toEntity());
    }

    @Override
    public void updateById(Long id, CategoriaDTO dto) {
        jpaRepository.findById(id).ifPresent(entity -> {
            entity.setName(dto.getName()); 
            jpaRepository.save(entity);
        });
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void saveAll(List<CategoriaDTO> categories) {
        List<categoria> entities = categories.stream()
                .map(CategoriaDTO::toEntity)
                .collect(Collectors.toList());
        jpaRepository.saveAll(entities);
    }

    @Override
    public Optional<CategoriaDTO> findByIdDTO(Long id) {
        return jpaRepository.findById(id).map(CategoriaDTO::convertToDTO);
    }
}
