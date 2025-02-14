package com.alternative.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.alternative.dto.request.UserDTO;
import com.alternative.entities.User;
@Component
public class UserMapper {
    private static final ProductMapper productMapper = new ProductMapper();
    private static final OrderMapper orderMapper = new OrderMapper();
    
    public UserDTO toDTO(User user) {
        if (user == null) return null;
        
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setCity(user.getCity());
        dto.setRole(user.getRole());
        dto.setPhoneNumber(user.getPhoneNumber());
        
        // Handle products
        if (user.getProducts() != null) {
            dto.setProducts(user.getProducts().stream()
                .map(product -> productMapper.toDTOWithoutCategory(product))
                .collect(Collectors.toList()));
        }
        
        // Handle orders
        if (user.getOrders() != null) {
            dto.setOrders(user.getOrders().stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    public UserDTO toDTOWithoutProducts(User user) {
        if (user == null) return null;
        
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setCity(user.getCity());
        dto.setRole(user.getRole());
        dto.setPhoneNumber(user.getPhoneNumber());
        return dto;
    }
    
    public UserDTO toDTOWithoutOrders(User user) {
        if (user == null) return null;
        
        UserDTO dto = toDTOWithoutProducts(user);
        if (user.getProducts() != null) {
            dto.setProducts(user.getProducts().stream()
                .map(product -> productMapper.toDTOWithoutCategory(product))
                .collect(Collectors.toList()));
        }
        return dto;
    }
    
    public User toEntity(UserDTO dto) {
        if (dto == null) return null;
        
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setCity(dto.getCity());
        user.setRole(dto.getRole());
        user.setPhoneNumber(dto.getPhoneNumber());
        
        if (dto.getProducts() != null) {
            user.setProducts(dto.getProducts().stream()
                .map(productDTO -> productMapper.toEntityWithoutCategory(productDTO))
                .collect(Collectors.toList()));
        }
        
        if (dto.getOrders() != null) {
            user.setOrders(dto.getOrders().stream()
                .map(orderMapper::toEntity)
                .collect(Collectors.toList()));
        }
        
        return user;
    }
    
    public User toEntityWithoutProducts(UserDTO dto) {
        if (dto == null) return null;
        
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setCity(dto.getCity());
        user.setRole(dto.getRole());
        user.setPhoneNumber(dto.getPhoneNumber());
        return user;
    }
    
    public User toEntityWithoutOrders(UserDTO dto) {
        if (dto == null) return null;
        
        User user = toEntityWithoutProducts(dto);
        if (dto.getProducts() != null) {
            user.setProducts(dto.getProducts().stream()
                .map(productDTO -> productMapper.toEntityWithoutCategory(productDTO))
                .collect(Collectors.toList()));
        }
        return user;
    }
}