package com.jaoui.microservicecommandes.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Commande {
    @Id
    @GeneratedValue
    private int id;

    private String description;

    private int quantite;

    private Date date;

    private double montant;
}
