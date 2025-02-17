package com.alternative.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alternative.ResourceNotFoundException.ResourceNotFoundException;
import com.alternative.dto.request.OrderDTO;
import com.alternative.dto.request.OrderItemDTO;
import com.alternative.entities.Order;
import com.alternative.entities.OrderItem;
import com.alternative.entities.Product;
import com.alternative.entities.User;
import com.alternative.mapper.OrderMapper;
import com.alternative.repositories.OrderRepository;
import com.alternative.repositories.ProductRepository;
import com.alternative.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
	@Autowired
    private  OrderRepository orderRepository;
	@Autowired
    private  OrderMapper orderMapper;
	@Autowired
    private ProductRepository productRepository;
	
	
	@Autowired
    private UserRepository userRepository;
    
   
	@Transactional
	public OrderDTO createOrder(OrderDTO orderDTO) {
	    // Validation des données d'entrée
	    if (orderDTO == null) {
	        throw new IllegalArgumentException("OrderDTO ne peut pas être null.");
	    }

	    // Convertir OrderDTO en entité Order
	    Order order = new Order();
	    order.setOrderDate(orderDTO.getOrderDate());
	    order.setStatus(orderDTO.getStatus());
	    order.setTotalAmount(orderDTO.getTotalAmount());

	    // Récupérer le client (utilisateur)
	    User client = userRepository.findById(orderDTO.getClient().getId())
	            .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'ID : " + orderDTO.getClient().getId()));
	    order.setClient(client);

	    // Récupérer le producteur (utilisateur)
	    User producer = userRepository.findById(orderDTO.getUser().getId())
	            .orElseThrow(() -> new ResourceNotFoundException("Producteur introuvable avec l'ID : " + orderDTO.getUser().getId()));
	    order.setUser(producer);

	    // Vérifier et créer les OrderItems
	    if (orderDTO.getOrderItems() != null && !orderDTO.getOrderItems().isEmpty()) {
	        List<OrderItem> orderItems = new ArrayList<>();

	        for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
	            // Vérifier si le produit existe
	            Product product = productRepository.findById(itemDTO.getProduct().getId())
	                    .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable avec l'ID : " + itemDTO.getProduct().getId()));

	            // Vérifier si la quantité en stock est suffisante
	            if (product.getQuantityInStock() < itemDTO.getQuantity()) {
	                throw new IllegalArgumentException("Quantité insuffisante en stock pour le produit avec l'ID : " + product.getId());
	            }

	            // Mettre à jour la quantité en stock
	            product.setQuantityInStock(product.getQuantityInStock() - itemDTO.getQuantity());
	            productRepository.save(product);  // Sauvegarder la mise à jour du produit

	            // Créer un nouvel OrderItem
	            OrderItem orderItem = new OrderItem();
	            orderItem.setQuantity(itemDTO.getQuantity());
	            orderItem.setProduct(product);
	            orderItem.setOrder(order);

	            orderItems.add(orderItem);
	        }

	        // Associer les OrderItems à la commande
	        order.setOrderItems(orderItems);
	    }

	    // Sauvegarder la commande dans la base de données
	    order = orderRepository.save(order);

	    // Convertir l'entité Order en OrderDTO et retourner le résultat
	    return orderMapper.toDTO(order);
	}
	   public List<OrderDTO> getOrdersByClient(Long clientId) {
	        // Vérifier si le client existe
	        userRepository.findById(clientId)
	                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'ID : " + clientId));

	        // Récupérer les commandes du client
	        List<Order> orders = orderRepository.findByClientId(clientId);
	        return orders.stream()
	                .map(orderMapper::toDTO)
	                .collect(Collectors.toList());
	    }

	    public List<OrderDTO> getOrdersByProducer(Long producerId) {
	        // Vérifier si le producteur existe
	        userRepository.findById(producerId)
	                .orElseThrow(() -> new ResourceNotFoundException("Producteur introuvable avec l'ID : " + producerId));

	        // Récupérer les commandes du producteur
	        List<Order> orders = orderRepository.findByUserId(producerId);
	        return orders.stream()
	                .map(orderMapper::toDTO)
	                .collect(Collectors.toList());
	    }
	
    
    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, String status, Long producerId) {
        Order order = orderRepository.findByIdAndUserId(orderId, producerId)
            .orElseThrow(() -> new RuntimeException("Order not found or unauthorized"));
        order.setStatus(status);
        return orderMapper.toDTO(orderRepository.save(order));
    }
    
    
    
    


}
