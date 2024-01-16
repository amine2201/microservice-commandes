package com.jaoui.microservicecommandes.controller;

import com.jaoui.microservicecommandes.configuration.ApplicationPropertiesConfiguration;
import com.jaoui.microservicecommandes.dao.CommandeDao;
import com.jaoui.microservicecommandes.exception.CommandeNotFoundException;
import com.jaoui.microservicecommandes.model.Commande;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@EnableCircuitBreaker
@Configuration
@EnableHystrixDashboard
@RestController
@AllArgsConstructor
@Slf4j
public class CommandeController implements HealthIndicator {
    private final CommandeDao commandeDao;
    private final ApplicationPropertiesConfiguration appProperties;

    // List all orders
    @GetMapping(value = "/commandes")
    public List<Commande> listAllOrders() {
        log.info("Listing all recent orders");
        List<Commande> orders = commandeDao.findAll();

        if (orders.isEmpty()) {
            log.error("No orders available");
            throw new CommandeNotFoundException("No orders available");
        }

        // Calculate the cutoff date
        Instant cutoffInstant = LocalDate.now().minusDays(appProperties.getCommandesLast())
                .atStartOfDay(ZoneId.systemDefault()).toInstant();
        Date cutoffDate = Date.from(cutoffInstant);

        return orders.stream()
                .filter(order -> order.getDate() != null && order.getDate().after(cutoffDate))
                .collect(Collectors.toList());
    }



    // Retrieve an order by its ID
    @GetMapping(value = "/commandes/{id}")
    public Commande getOrderById(@PathVariable int id) throws InterruptedException {
        log.info("Retrieving order with ID {}", id);
        return commandeDao.findById(id).orElseThrow(() -> {
            log.error("Order with ID {} not found", id);
            return new CommandeNotFoundException("Order with ID " + id + " does not exist");
        });
    }

    @HystrixCommand(fallbackMethod = "HistrixbuildFallbackMessage",
            commandProperties ={@HystrixProperty(name =
                    "execution.isolation.thread.timeoutInMilliseconds", value = "1000")},
            threadPoolKey = "messageThreadPool")
    @GetMapping("/myMessage")
    public String getMessage() throws InterruptedException {
        log.info("Message from CommandeController.getMessage(): Begin To sleep for 3 scondes ");
        Thread.sleep(3000);
        return "Message from CommandeController.getMessage(): End from sleep for 3 secondes ";
    }

    private String HistrixbuildFallbackMessage() {
        log.info("Message from HistrixbuildFallbackMessage() : Hystrix Fallback message ( after timeout : 1 second )");
        return "Message from HistrixbuildFallbackMessage() : Hystrix Fallback message ( after timeout : 1 second )";
    }

    //Create an order
    @PostMapping(value = "/commandes")
    public Commande createOrder(@RequestBody Commande order) {
        log.info("Creating order {}", order);
        return commandeDao.save(order);
    }

    // Health check
    @Override
    public Health health() {
        log.info("Performing health check");
        List<Commande> orders = commandeDao.findAll();
        if (orders.isEmpty()) {
            log.warn("Health check status down: No orders found");
            return Health.down().build();
        }
        log.info("Health check status up");
        return Health.up().build();
    }
}
