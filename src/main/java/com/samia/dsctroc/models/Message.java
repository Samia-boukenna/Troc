package com.samia.dsctroc.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
public class Message {

    @Id @GeneratedValue
    private int id;

    private String msgId;

    private Date dte;

    private int dureeValide;

    @OneToOne
    private Dmd dmd;

    @OneToOne
    private Auth auth;

    @OneToOne
    private Prop prop;

    @OneToOne
    private Accep accep;

    @ManyToOne
    @JoinColumn(name = "fichier")
    private Fichier fichier;

    public Message(){
        msgId = "_"+UUID.randomUUID().toString();
    }

}
