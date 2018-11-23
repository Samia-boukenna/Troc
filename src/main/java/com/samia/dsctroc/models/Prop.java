package com.samia.dsctroc.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
public class Prop {

    @Id
    @GeneratedValue
    private int id;

    private String titre;

    // type soit offre soit demande
    private String type;

    @OneToMany(mappedBy = "prop")
    private List<Objet> objets;

}
