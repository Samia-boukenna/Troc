package com.samia.dsctroc.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@Entity
public class Accep {
    @Id
    @GeneratedValue
    private int id;

    private String messageValid;

    @OneToOne
    private Prop contreProp;

}
