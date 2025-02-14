package com.alternative.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alternative.entities.Product;
import com.alternative.entities.Review;
import com.alternative.entities.User;



@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{

	List<Review> findByProduct(Product product);
	List<Review> findByClient(User user);
}
