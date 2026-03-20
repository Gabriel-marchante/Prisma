package org.padalustro.infrastructure.DTO
;

import java.io.Serializable;

import org.padalustro.domain.entities.categoria;

import lombok.Data;

@Data
public class CategoriaDTO implements Serializable {
    private Long id;
    private String name;

    public CategoriaDTO() {}

    public CategoriaDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoriaDTO convertToDTO(categoria categoria) {
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(categoria.getId());
        categoriaDTO.setName(categoria.getName());
        return categoriaDTO;
    }

    public categoria toEntity() {
        categoria categoria = new categoria();
        categoria.setId(this.id);
        categoria.setName(this.name);
        return categoria;
    }

    
}
