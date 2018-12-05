package com.samia.dsctroc.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Auth {
    @Id
    @GeneratedValue
    private int id;

    private String messageAuth = "demande traité";

    private boolean accepte;

    private String numAuth;
}
