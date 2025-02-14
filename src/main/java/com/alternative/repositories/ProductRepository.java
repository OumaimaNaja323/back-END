package com.alternative.repositories;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alternative.entities.User;
import com.alternative.dto.request.ProductDTO;
import com.alternative.entities.Category;
import com.alternative.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByUser(User user);
	List<Product> findByCategory(Category categorie);


    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);
    
    @Query("SELECT p FROM Product p WHERE p.user.id = :userId")
    List<Product> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT p FROM Product p WHERE p.id = :productId AND p.user.id = :userId")
    Optional<Product> findByIdAndUserId(@Param("productId") Long productId, @Param("userId") Long userId);
}
