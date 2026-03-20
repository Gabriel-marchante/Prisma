package org.padalustro.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.padalustro.domain.entities.video_cataleg;
import org.padalustro.domain.repository.VideoCatalegRepository;
import org.padalustro.infrastructure.DTO.VideoCatalegDTO;
import org.padalustro.infrastructure.repository.jpa.CategoriaJpaRepository;
import org.padalustro.infrastructure.repository.jpa.EstudiJpaRepository;
import org.padalustro.infrastructure.repository.jpa.SerieJpaRepository;
import org.padalustro.infrastructure.repository.jpa.VideoCatalegJpaRepository;
import org.springframework.stereotype.Service;



@Service
public class VideoCatalegRepositoryImpl implements VideoCatalegRepository {

    private final VideoCatalegJpaRepository jpaRepository;
    private final CategoriaJpaRepository categoriaJpaRepository;
    private final EstudiJpaRepository estudiJpaRepository;
    private final SerieJpaRepository serieJpaRepository;



    public VideoCatalegRepositoryImpl(
        VideoCatalegJpaRepository jpaRepository,
        CategoriaJpaRepository categoriaJpaRepository,
        EstudiJpaRepository estudiJpaRepository,
        SerieJpaRepository serieJpaRepository
) {
    this.jpaRepository = jpaRepository;
    this.categoriaJpaRepository = categoriaJpaRepository;
    this.estudiJpaRepository = estudiJpaRepository;
    this.serieJpaRepository = serieJpaRepository;
}


    @Override
    public List<VideoCatalegDTO> listAllVideoCataleg() {
        return jpaRepository.findAll()
                .stream()
                .map(VideoCatalegDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VideoCatalegDTO getVideoCatalegById(Long id) {
        return jpaRepository.findById(id)
                .map(VideoCatalegDTO::convertToDTO)
                .orElse(null);
    }

    @Override
    public boolean deleteVideoCataleg(Long id) {
        Optional<video_cataleg> video = jpaRepository.findById(id);
        if (video.isPresent()) {
            jpaRepository.delete(video.get());
            return true;
        }
        return false;
    }

    @Override

    public void saveVideoCataleg(VideoCatalegDTO dto) {
        video_cataleg entity = dto.toEntity();
        jpaRepository.save(entity);
    }
    
    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<VideoCatalegDTO> findAllDTO() {
        return listAllVideoCataleg();
    }

    @Override
    public Optional<VideoCatalegDTO> findByIdDTO(Long id) {
        return jpaRepository.findById(id).map(VideoCatalegDTO::convertToDTO);
    }

    @Override
    public Optional<video_cataleg> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public void delete(video_cataleg video_cataleg) {
        jpaRepository.delete(video_cataleg);
    }
    @Override
    public void updateVideoCataleg(Long id, VideoCatalegDTO dto) {
        video_cataleg entity = jpaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Video no encontrado"));
    
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setRating(dto.getRating());
        entity.setSeason(dto.getSeason());
        entity.setChapter(dto.getChapter());
        entity.setDuration(dto.getDuration());
    
        // put para poder actualizar una categoria de una peli
        if (dto.getCategory() != null) {
            entity.setCategory(
                dto.getCategory().stream()
                    .map(c -> categoriaJpaRepository.findById(c.getId())
                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + c.getId())))
                    .collect(Collectors.toSet())
            );
        }
    
        // put para studi de una peli
        if (dto.getStudy() != null) {
            entity.setStudy(
                estudiJpaRepository.findById(dto.getStudy().getId())
                    .orElseThrow(() -> new RuntimeException("Estudio no encontrado"))
            );
        }
    
        // put para actualizar serie de una peli
        if (dto.getSeries() != null) {
            entity.setSeries(
                serieJpaRepository.findById(dto.getSeries().getId())
                    .orElseThrow(() -> new RuntimeException("Serie no encontrada"))
            );
        }
    
        jpaRepository.save(entity);
    }
}
