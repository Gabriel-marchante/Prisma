package org.padalustro.infrastructure.DTO.IDs;

import java.io.Serializable;

import lombok.Data;

@Data
public class CategoriaIdDTO implements Serializable {
    private Long id;

    public CategoriaIdDTO() {}

    public CategoriaIdDTO(Long id) {
        this.id = id;
    }
}
