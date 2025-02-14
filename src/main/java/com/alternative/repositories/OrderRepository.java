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

	@Query("SELECT o FROM Order o " +
	           "LEFT JOIN FETCH o.orderItems items " +
	           "LEFT JOIN FETCH items.product " +
	           "WHERE o.client.id = :clientId")
	    List<Order> findByClientId(@Param("clientId") Long clientId);
	    
	    @Query("SELECT o FROM Order o " +
	           "LEFT JOIN FETCH o.orderItems items " +
	           "LEFT JOIN FETCH items.product " +
	           "WHERE o.user.id = :producerId")
	    List<Order> findByUserId(@Param("producerId") Long producerId);
	    
	    @Query("SELECT o FROM Order o " +
	           "LEFT JOIN FETCH o.orderItems items " +
	           "LEFT JOIN FETCH items.product " +
	           "WHERE o.id = :orderId AND o.user.id = :producerId")
	    Optional<Order> findByIdAndUserId(@Param("orderId") Long orderId, 
	                                    @Param("producerId") Long producerId);
	
}
