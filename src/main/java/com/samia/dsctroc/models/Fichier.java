package com.samia.dsctroc.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Fichier {

    @Id
    @GeneratedValue
    private int id;

    private String ficId;

    private String numAuto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emetteur")
    private Utilisateur iE;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recepteur")
    private Utilisateur iR;
    @OneToMany(mappedBy = "fichier")
    List<Message> messages;

    public Fichier(){
        ficId = UUID.randomUUID().toString();
    }
}
