package com.jaoui.microservicecommandes.dao;

import com.jaoui.microservicecommandes.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeDao extends JpaRepository<Commande, Integer> {
}
