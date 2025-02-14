package com.alternative.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alternative.dto.UserReq;
import com.alternative.services.AuthService;


@RestController
public class AuthController {
	
	@Autowired
    private AuthService authService;
	
	


    @PostMapping("/auth/register")
    public ResponseEntity<UserReq> regeister(@RequestBody UserReq reg){
        return ResponseEntity.ok(authService.register(reg));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UserReq> login(@RequestBody UserReq req){
        return ResponseEntity.ok(authService.login(req));
    }

   @PostMapping("/auth/refresh")
   public ResponseEntity<UserReq> refreshToken(@RequestBody UserReq req){
	   return ResponseEntity.ok(authService.refreshToken(req));
   }
   

    
  


}