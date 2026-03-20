package org.padalustro.infrastructure.DTO
;

import java.io.Serializable;

import org.padalustro.domain.entities.estudi;

import lombok.Data;

@Data
public class EstudiDTO implements Serializable {
    private Long id;
    private String name;

    public EstudiDTO() {}

    public EstudiDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static EstudiDTO convertToDTO(estudi estudi) {
        EstudiDTO EstudiDTO = new EstudiDTO();
        EstudiDTO.setId(estudi.getId());
        EstudiDTO.setName(estudi.getName());
        return EstudiDTO;
    }

    public estudi toEntity() {
        estudi estudi = new estudi();
        estudi.setId(this.id);
        estudi.setName(this.name);
        return estudi;
    }

    
}
