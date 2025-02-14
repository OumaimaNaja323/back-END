package com.alternative.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alternative.dto.request.OrderDTO;
import com.alternative.entities.Order;
import com.alternative.entities.OrderItem;
import com.alternative.entities.Product;
import com.alternative.entities.User;
import com.alternative.mapper.OrderMapper;
import com.alternative.repositories.OrderRepository;
import com.alternative.repositories.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
	@Autowired
    private  OrderRepository orderRepository;
	@Autowired
    private  OrderMapper orderMapper;
	@Autowired
    private ProductRepository productRepository;
    
   
    
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO, Long clientId) {
        Order order = orderMapper.toEntity(orderDTO);
        order.setClient(new User()); // Set current client
        order.getClient().setId(clientId);
        
        // Update product quantities and validate stock
        for (OrderItem item : order.getOrderItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
                
            if (product.getQuantityInStock() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            
            product.setQuantityInStock(product.getQuantityInStock() - item.getQuantity());
            productRepository.save(product);
        }
        
        return orderMapper.toDTO(orderRepository.save(order));
    }
    
    public List<OrderDTO> getClientOrders(Long clientId) {
        return orderRepository.findByClientId(clientId).stream()
            .map(orderMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<OrderDTO> getProducerOrders(Long producerId) {
        return orderRepository.findByUserId(producerId).stream()
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
