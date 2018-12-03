package com.samia.dsctroc.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Objet {

    @Id
    @GeneratedValue
    private int id;

    private String type;
    @OneToOne
    private Description description;

    @ManyToOne
    @JoinColumn(name = "prop")
    private Prop prop;
}
