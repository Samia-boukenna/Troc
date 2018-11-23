package com.samia.dsctroc.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
public class Utilisateur {
    @Id
    @GeneratedValue
    private int id;

    private String nom;

    private String prenom;

    private String mail;

    private String motDePasse;

    @OneToMany(mappedBy = "iE")
    private List<Fichier> fichiersEm;
    @OneToMany(mappedBy = "iR")
    private List<Fichier> fichiersRec;

}
