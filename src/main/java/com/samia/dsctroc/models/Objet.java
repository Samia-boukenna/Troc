package com.samia.dsctroc.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Objet {

    @Id
    @GeneratedValue
    private int id;

    private String nom;
    private String type;
    private String valeur;

    @ManyToOne
    @JoinColumn(name = "prop")
    private Prop prop;
}
