/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samia.dsctroc.models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Data;

/**
 *
 * @author fdr
 */
@Data
@Entity
public class Parametre {
    @Id
    @GeneratedValue
    private int id;
    private String nom;
    private String valeur;
}
