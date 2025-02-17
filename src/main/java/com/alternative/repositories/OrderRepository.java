package com.alternative.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alternative.entities.Order;
import com.alternative.entities.OrderItem;



@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Récupère toutes les commandes d'un client avec leurs éléments de commande et produits associés.
     *
     * @param clientId L'ID du client.
     * @return Une liste de commandes associées au client.
     */
    @Query("SELECT DISTINCT o FROM Order o " +
           "LEFT JOIN FETCH o.orderItems items " +
           "LEFT JOIN FETCH items.product " +
           "WHERE o.client.id = :clientId")
    List<Order> findByClientId(@Param("clientId") Long clientId);

    /**
     * Récupère toutes les commandes d'un producteur avec leurs éléments de commande et produits associés.
     *
     * @param producerId L'ID du producteur.
     * @return Une liste de commandes associées au producteur.
     */
    @Query("SELECT DISTINCT o FROM Order o " +
           "LEFT JOIN FETCH o.orderItems items " +
           "LEFT JOIN FETCH items.product " +
           "WHERE o.user.id = :producerId")
    List<Order> findByUserId(@Param("producerId") Long producerId);

    /**
     * Récupère une commande spécifique pour un producteur donné, avec ses éléments de commande et produits associés.
     *
     * @param orderId    L'ID de la commande.
     * @param producerId L'ID du producteur.
     * @return La commande correspondante, si elle existe.
     */
    @Query("SELECT DISTINCT o FROM Order o " +
           "LEFT JOIN FETCH o.orderItems items " +
           "LEFT JOIN FETCH items.product " +
           "WHERE o.id = :orderId AND o.user.id = :producerId")
    Optional<Order> findByIdAndUserId(@Param("orderId") Long orderId, 
                                     @Param("producerId") Long producerId);
}
	

