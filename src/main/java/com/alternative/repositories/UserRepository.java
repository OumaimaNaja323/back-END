package com.alternative.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alternative.entities.Order;
import com.alternative.entities.Product;
import com.alternative.entities.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
    List<User>findByRole(String role);
	User findByEmail(String email);
	
	
	
	
}
