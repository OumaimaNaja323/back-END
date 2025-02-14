package com.alternative.services;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alternative.dto.UserReq;
import com.alternative.entities.User;
import com.alternative.repositories.UserRepository;



@Service
public class AuthService {

    @Autowired
    private UserRepository usersRepo;
    
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserReq register(UserReq registrationRequest){
    	
    	UserReq resp = new UserReq();
    
        try {
        	User ourUser = new User();
            ourUser.setEmail(registrationRequest.getEmail());
            ourUser.setCity(registrationRequest.getCity());
            ourUser.setRole(registrationRequest.getRole());
            ourUser.setName(registrationRequest.getName());
            ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            User ourUsersResult = usersRepo.save(ourUser);
            if (ourUsersResult.getId()>0) {
                resp.setOurUsers((ourUsersResult));
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }
          

        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public UserReq login(UserReq loginRequest) {
        UserReq response = new UserReq();
        try {
            // Authenticate the user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword()));

            // Retrieve the user from the database
            var user = usersRepo.findByEmail(loginRequest.getEmail());

            if (user == null) {
                throw new UsernameNotFoundException("User not found with email: " + loginRequest.getEmail());
            }

            // Generate the JWT token
            var jwt = jwtUtils.generateToken(user);

            // Generate the Refresh Token
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<String, Object>(), user);

            // Set response attributes
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken); // Add refresh token to response if needed
            response.setRole(user.getRole());
            response.setRedirectUrl(getUrl(user.getRole()));
            
            
           // response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");

        } catch (BadCredentialsException e) {
            // Handle invalid credentials
            response.setStatusCode(401);
            response.setMessage("Invalid email or password");
        } catch (UsernameNotFoundException e) {
            // Handle user not found
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            // Catch any other exceptions
            response.setStatusCode(500);
            response.setMessage("An error occurred: " + e.getMessage());
        }
        return response;
    }

    private String getUrl(String role) {
    	return switch(role) {
        case "PRODUCER" -> "/producteur/index";
        case "USER" -> "/user/index" ;
        case "ADMIN" -> "/admin/index";
        default -> "/auth/login" ;
        	
    	};
    }




    public UserReq refreshToken(UserReq refreshTokenReqiest){
    	UserReq response = new UserReq();
        try{
            String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
            User users = usersRepo.findByEmail(ourEmail);
            if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
                var jwt = jwtUtils.generateToken(users);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenReqiest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
            return response;

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }


    






}