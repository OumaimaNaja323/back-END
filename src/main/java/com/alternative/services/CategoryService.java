package com.alternative.services;



import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.alternative.dto.request.CategoryDTO;
import com.alternative.entities.Category;
import com.alternative.mapper.CategoryMapper;
import com.alternative.repositories.CategoryRepository;

import jakarta.transaction.Transactional;

@Service
public class CategoryService {
	@Autowired
    private  CategoryRepository categoryRepository;
	@Autowired
    private  CategoryMapper categoryMapper;
    
   
	 // Récupérer toutes les catégories
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(categoryMapper::toDTO)
            .collect(Collectors.toList());
    }

    // Récupérer une catégorie par son ID
    public CategoryDTO getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(categoryMapper::toDTO).orElse(null);
    }

  
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = categoryMapper.toEntity(categoryDTO);
        return categoryMapper.toDTO(categoryRepository.save(category));
    }

    // Mettre à jour une catégorie existante
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(categoryDTO.getName());
                    category.setDescription(categoryDTO.getDescription()); // Assurez-vous d’inclure tous les champs
                    return categoryMapper.toDTO(categoryRepository.save(category));
                })
                .orElse(null);
    }

    // Supprimer une catégorie par son ID
    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}