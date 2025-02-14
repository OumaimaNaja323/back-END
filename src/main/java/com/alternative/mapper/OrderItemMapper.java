package com.alternative.mapper;

import org.springframework.stereotype.Component;

import com.alternative.dto.request.OrderItemDTO;
import com.alternative.entities.OrderItem;
@Component
public class OrderItemMapper {
    private static final ProductMapper productMapper = new ProductMapper();
    
    public OrderItemDTO toDTO(OrderItem orderItem) {
        if (orderItem == null) return null;
        
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(orderItem.getId());
        dto.setQuantity(orderItem.getQuantity());
        
        if (orderItem.getProduct() != null) {
            dto.setProduct(productMapper.toDTO(orderItem.getProduct()));
        }
        
        return dto;
    }
    
    public OrderItem toEntity(OrderItemDTO dto) {
        if (dto == null) return null;
        
        OrderItem orderItem = new OrderItem();
        orderItem.setId(dto.getId());
        orderItem.setQuantity(dto.getQuantity());
        
        if (dto.getProduct() != null) {
            orderItem.setProduct(productMapper.toEntity(dto.getProduct()));
        }
        
        return orderItem;
    }
}

