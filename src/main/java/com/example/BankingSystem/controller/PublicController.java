package com.example.BankingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BankingSystem.entity.AdminEntity;
import com.example.BankingSystem.service.PublicService;

@RestController
@RequestMapping("/login")
public class PublicController {
	
	@Autowired
	private PublicService publicService;
	
	@PostMapping
	public ResponseEntity<String> getUser(@RequestBody AdminEntity adminEntity) {
		String role = publicService.get(adminEntity);
		if(role != null) {
			return new ResponseEntity<>(role,HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	

}
