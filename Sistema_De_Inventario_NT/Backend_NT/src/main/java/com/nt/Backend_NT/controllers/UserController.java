package com.nt.Backend_NT.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nt.Backend_NT.entities.UserEntity;
import com.nt.Backend_NT.model.ErrorResponse;
import com.nt.Backend_NT.services.UserService;


@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping
	public ResponseEntity createUser(@RequestBody UserEntity user, @RequestParam String adminPassword)
			throws Exception{
		
		UserEntity newUser =  userService.createUserEntity(user, adminPassword);
		
		return new ResponseEntity<UserEntity>(newUser, HttpStatus.CREATED);
	
	}

}
