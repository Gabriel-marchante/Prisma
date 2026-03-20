package org.padalustro.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "serie")
public class serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "serie_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int  classification;

    public serie() {
    }

    public serie(String name, int classification) {
        this.name = name;
        this.classification = classification;
    }

    // getters / setters (Lombok ja els crea, però els deixe explícits si els uses)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClassification() {
        return classification;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }
}
