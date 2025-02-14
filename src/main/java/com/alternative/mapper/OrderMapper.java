package com.alternative.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.alternative.dto.request.*;
import com.alternative.entities.Order;
@Component
public class OrderMapper {
    private static final UserMapper userMapper = new UserMapper();
    private static final OrderItemMapper orderItemMapper = new OrderItemMapper();
    
    public OrderDTO toDTO(Order order) {
        if (order == null) return null;
        
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        
        // Handle client
        if (order.getClient() != null) {
            dto.setClient(userMapper.toDTOWithoutOrders(order.getClient()));
        }
        
        // Handle user (producer)
        if (order.getUser() != null) {
            dto.setUser(userMapper.toDTOWithoutOrders(order.getUser()));
        }
        
        // Handle order items
        if (order.getOrderItems() != null) {
            dto.setOrderItems(order.getOrderItems().stream()
                .map(orderItemMapper::toDTO)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    public Order toEntity(OrderDTO dto) {
        if (dto == null) return null;
        
        Order order = new Order();
        order.setId(dto.getId());
        order.setOrderDate(dto.getOrderDate());
        order.setStatus(dto.getStatus());
        order.setTotalAmount(dto.getTotalAmount());
        
        if (dto.getClient() != null) {
            order.setClient(userMapper.toEntityWithoutOrders(dto.getClient()));
        }
        
        if (dto.getUser() != null) {
            order.setUser(userMapper.toEntityWithoutOrders(dto.getUser()));
        }
        
        if (dto.getOrderItems() != null) {
            order.setOrderItems(dto.getOrderItems().stream()
                .map(orderItemMapper::toEntity)
                .collect(Collectors.toList()));
        }
        
        return order;
    }
}
