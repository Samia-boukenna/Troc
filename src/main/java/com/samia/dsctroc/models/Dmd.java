package com.samia.dsctroc.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Dmd {

    @Id
    @GeneratedValue
    private int id;

    private String description;
    private Date dateDebut;
    private Date dateFin;
}
