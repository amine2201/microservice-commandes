package com.jaoui.microservicecommandes.configuration;

import com.jaoui.microservicecommandes.dao.CommandeDao;
import com.jaoui.microservicecommandes.model.Commande;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Calendar;
import java.util.Date;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner commandLineRunner(CommandeDao commandeDao, ApplicationPropertiesConfiguration appProperties) {
        return args -> {
            Calendar calendar = Calendar.getInstance();

            // Creating new Orders with dates from 1 to 10 days ago
            for (int i = 1; i <= 20; i++) {
                calendar.setTime(new Date()); // reset to current date
                calendar.add(Calendar.DATE, -i); // set to 'i' days ago

                Commande commande = new Commande(i, "Product " + i, i, calendar.getTime(), 50D + i);
                commandeDao.save(commande);
            }
        };
    }
}
