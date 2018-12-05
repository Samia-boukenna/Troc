package com.samia.dsctroc.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import javax.persistence.OneToOne;

@Data
@Entity
public class Prop {

    @Id
    @GeneratedValue
    private int id;

    private String titre;

    @OneToOne
    private Offre offre;
    @OneToOne
    private Demande demande;
    

}
