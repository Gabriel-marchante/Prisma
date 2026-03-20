package org.padalustro.infrastructure.DTO;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

import org.padalustro.domain.entities.categoria;
import org.padalustro.domain.entities.estudi;
import org.padalustro.domain.entities.serie;
import org.padalustro.domain.entities.video_cataleg;
import org.padalustro.infrastructure.DTO.IDs.CategoriaIdDTO;
import org.padalustro.infrastructure.DTO.IDs.EstudiIdDTO;
import org.padalustro.infrastructure.DTO.IDs.SerieIdDTO;

import lombok.Data;

@Data
public class VideoCatalegDTO implements Serializable {

    private Long id;
    private String title;
    private String description;
    private Set<CategoriaIdDTO> category;
    private EstudiIdDTO study;
    private double rating;
    private Integer season;
    private SerieIdDTO series;
    private Integer chapter;
    private Integer duration;
    private String codec;
    private String resolucio;
    private long pes;

    public static VideoCatalegDTO convertToDTO(video_cataleg videoCataleg) {
        VideoCatalegDTO dto = new VideoCatalegDTO();

        dto.setId(videoCataleg.getId());
        dto.setTitle(videoCataleg.getTitle());
        dto.setDescription(videoCataleg.getDescription());

        if (videoCataleg.getCategory() != null) {
            dto.setCategory(videoCataleg.getCategory().stream()
                    .map(c -> new CategoriaIdDTO(c.getId()))
                    .collect(Collectors.toSet()));
        }

        if (videoCataleg.getStudy() != null) {
            dto.setStudy(new EstudiIdDTO(videoCataleg.getStudy().getId()));
        }

        dto.setRating(videoCataleg.getRating());
        dto.setSeason(videoCataleg.getSeason());

        if (videoCataleg.getSeries() != null) {
            dto.setSeries(new SerieIdDTO(videoCataleg.getSeries().getId()));
        }

        dto.setChapter(videoCataleg.getChapter());
        dto.setDuration(videoCataleg.getDuration());

        // nous camps
        dto.setCodec(videoCataleg.getCodec());
        dto.setResolucio(videoCataleg.getResolucio());
        dto.setPes(videoCataleg.getPes());

        return dto;
    }

    public video_cataleg toEntity() {
        video_cataleg entity = new video_cataleg();

        entity.setId(this.getId());
        entity.setTitle(this.getTitle());
        entity.setDescription(this.getDescription());

        if (this.getCategory() != null) {
            entity.setCategory(this.getCategory().stream()
                    .map(c -> {
                        categoria cat = new categoria();
                        cat.setId(c.getId());
                        return cat;
                    })
                    .collect(Collectors.toSet()));
        }

        if (this.getStudy() != null) {
            estudi study = new estudi();
            study.setId(this.getStudy().getId());
            entity.setStudy(study);
        }

        entity.setRating(this.getRating());
        entity.setSeason(this.getSeason());

        if (this.getSeries() != null) {
            serie series = new serie();
            series.setId(this.getSeries().getId());
            entity.setSeries(series);
        }

        entity.setChapter(this.getChapter());
        entity.setDuration(this.getDuration());

        // nous camps
        entity.setCodec(this.getCodec());
        entity.setResolucio(this.getResolucio());
        entity.setPes(this.getPes());

        return entity;
    }
}
