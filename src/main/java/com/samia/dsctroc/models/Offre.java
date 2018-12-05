package com.samia.dsctroc.models;

import java.util.List;
import java.util.Set;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Offre {

    @Id
    @GeneratedValue
    private int id;
    @ManyToMany
    private Set<Objet> objets;
}
