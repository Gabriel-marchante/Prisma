package org.padalustro.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

// V many to one usuari
@Data
@Entity
@Table(name = "perfil")
public class perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfil_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean infantile = false;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    private usuari user;


    
    public perfil() {
    }



    public perfil(String name, boolean infantile, usuari user) {
        this.name = name;
        this.infantile = infantile;
        this.user = user;
    }



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



    public boolean isInfantile() {
        return infantile;
    }



    public void setInfantile(boolean infantile) {
        this.infantile = infantile;
    }



    public usuari getUser() {
        return user;
    }



    public void setUser(usuari user) {
        this.user = user;
    }


    

}