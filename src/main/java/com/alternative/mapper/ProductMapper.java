package com.alternative.mapper;

import org.springframework.stereotype.Component;

import com.alternative.dto.request.*;
import com.alternative.entities.Product;

@Component
public class ProductMapper {
    private static final CategoryMapper categoryMapper = new CategoryMapper();
    private static final UserMapper userMapper = new UserMapper();

    // Convertir une entité Product en ProductDTO
    public ProductDTO toDTO(Product product) {
        if (product == null) return null;

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantityInStock(product.getQuantityInStock());
        dto.setImageUrl(product.getImageUrl());

        // Gérer la catégorie
        if (product.getCategory() != null) {
            dto.setCategory(categoryMapper.toDTO(product.getCategory()));
        }

        // Gérer l'utilisateur
        if (product.getUser() != null) {
            dto.setUser(userMapper.toDTOWithoutProducts(product.getUser()));
        }

        return dto;
    }

    // Convertir une entité Product en ProductDTO sans la catégorie
    public ProductDTO toDTOWithoutCategory(Product product) {
        if (product == null) return null;

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantityInStock(product.getQuantityInStock());
        dto.setImageUrl(product.getImageUrl());

        if (product.getUser() != null) {
            dto.setUser(userMapper.toDTOWithoutProducts(product.getUser()));
        }

        return dto;
    }

    // Convertir un ProductDTO en entité Product
    public Product toEntity(ProductDTO dto) {
        if (dto == null) return null;

        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantityInStock(dto.getQuantityInStock());
        product.setImageUrl(dto.getImageUrl());

        if (dto.getCategory() != null) {
            product.setCategory(categoryMapper.toEntity(dto.getCategory()));
        }

        if (dto.getUser() != null) {
            product.setUser(userMapper.toEntityWithoutProducts(dto.getUser()));
        }

        return product;
    }

    // Convertir un ProductDTO en entité Product sans la catégorie
    public Product toEntityWithoutCategory(ProductDTO dto) {
        if (dto == null) return null;

        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantityInStock(dto.getQuantityInStock());
        product.setImageUrl(dto.getImageUrl());

        if (dto.getUser() != null) {
            product.setUser(userMapper.toEntityWithoutProducts(dto.getUser()));
        }

        return product;
    }

    // Mettre à jour une entité Product existante avec les données d'un ProductDTO
    public void updateEntity(Product product, ProductDTO dto) {
        if (product == null || dto == null) return;

        // Mettre à jour les champs de l'entité Product
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantityInStock(dto.getQuantityInStock());
        product.setImageUrl(dto.getImageUrl());

        // Mettre à jour la catégorie si elle est fournie dans le DTO
        if (dto.getCategory() != null) {
            product.setCategory(categoryMapper.toEntity(dto.getCategory()));
        }

        // Mettre à jour l'utilisateur si il est fourni dans le DTO
        if (dto.getUser() != null) {
            product.setUser(userMapper.toEntityWithoutProducts(dto.getUser()));
        }
    }
}