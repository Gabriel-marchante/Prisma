package org.padalustro.domain.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

// V one to many perfil
@Data
@Entity
@Table(name = "usuari")
public class usuari {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

   

    public Long getId() {
        return id;
    }



    public usuari() {
    }

    
    

}
