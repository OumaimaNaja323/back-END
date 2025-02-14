package com.alternative.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.alternative.dto.UserReq;
import com.alternative.entities.Product;
import com.alternative.entities.User;
import com.alternative.repositories.ProductRepository;
import com.alternative.repositories.UserRepository;



@Service
public class AdminService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
    public UserReq getClients() {
    	UserReq resp = new UserReq();
    	
    	List<User> clients = userRepository.findByRole("USER");
    	resp.setStatusCode(200);
    	resp.setOurUsersList(clients);
    	resp.setMessage("Success");
    	
    	return resp;
    }
    
    public UserReq getProducers() {
    	UserReq resp = new UserReq();
    	
    	List<User> clients = userRepository.findByRole("PRODUCER");
    	resp.setStatusCode(200);
    	resp.setOurUsersList(clients);
    	resp.setMessage("Success");
    	
    	return resp;
    }
    
    public UserReq getAll() {
    	UserReq resp = new UserReq();
    	
    	List<User> all = userRepository.findAll();
    	resp.setStatusCode(200);
    	resp.setOurUsersList(all);
    	resp.setMessage("Success");
    	
    	return resp;
    }
  
	
   
    
    public UserReq getClient(Long id) {
    	UserReq resp = new UserReq();
    
    	Optional<User> client = userRepository.findById(id);
    	resp.setStatusCode(200);
    	resp.setClient(client);
    	
    	
    	return resp;
    	
    }
    
    public UserReq getProducer(Long id) {
    	UserReq resp = new UserReq();
    	Optional<User> producer = userRepository.findById(id);
        

    	resp.setStatusCode(200);
    	resp.setClient(producer);
 
    	return resp;
    	
    } 
    
    
    public UserReq getProducts(Long id) {
    	UserReq resp = new UserReq();
    	User producer = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    	List<Product> products = productRepository.findByUser(producer);
    	resp.setProducts(products);
    	resp.setMessage("Les Produit");
    	return resp;
    }
    

    public UserReq updateUser(Long id,@RequestBody UserReq req) {
    	UserReq resp = new UserReq();
    	
        try {
        	User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        	user.setName(req.getName());
        	user.setCity(req.getCity());
        	user.setEmail(req.getEmail());
        	user.setRole(req.getRole());
        	user.setPassword(passwordEncoder.encode(req.getPassword()));
        	user.setPhoneNumber(req.getPhoneNumber());
        	resp.setMessage("User Updated With Success");
        	User ourUser = userRepository.save(user);
        	
        }
        catch(Exception e) {
        resp.setMessage("error");
        }
        return resp;

    } 
    
    public UserReq deleteUser(Long id) {
    	UserReq resp = new UserReq();
    	userRepository.deleteById(id);
    	resp.setStatusCode(200);
        resp.setMessage("DELETED WITH SUCCESS");
    	return resp;
    }
    
    
 
	
}
