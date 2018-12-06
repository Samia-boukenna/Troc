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
    private long id;

    private String ficid;

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
        ficid = UUID.randomUUID().toString();
    }
}
