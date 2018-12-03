package com.samia.dsctroc.models;

import java.util.List;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Offre {

    @Id
    @GeneratedValue
    private int id;
    @OneToMany
    private List<Objet> objets;
}
