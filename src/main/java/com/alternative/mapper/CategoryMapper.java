package com.alternative.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import com.alternative.entities.*;
import com.alternative.dto.*;
import com.alternative.dto.request.*;

@Component
public class CategoryMapper {
    private static final ProductMapper productMapper = new ProductMapper();

    public CategoryDTO toDTO(Category category) {
        if (category == null) return null;
        
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        
//        // Handle products without causing infinite recursion
//        if (category.getProducts() != null) {
//            dto.setProducts(category.getProducts().stream()
//                .map(product -> productMapper.toDTOWithoutCategory(product))
//                .collect(Collectors.toList()));
//        }
        
        return dto;
    }
    
    public Category toEntity(CategoryDTO dto) {
        if (dto == null) return null;
        
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        
//        if (dto.getProducts() != null) {
//            category.setProducts(dto.getProducts().stream()
//                .map(productDTO -> productMapper.toEntityWithoutCategory(productDTO))
//                .collect(Collectors.toList()));
//        }
        
        return category;
    }
}