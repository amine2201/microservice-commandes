package com.jaoui.microservicecommandes.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
