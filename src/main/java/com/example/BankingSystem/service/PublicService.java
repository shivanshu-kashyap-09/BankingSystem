package com.example.BankingSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BankingSystem.entity.AdminEntity;
import com.example.BankingSystem.repository.AdminRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PublicService {

    @Autowired
    private AdminRepository adminRepository;

    public String get(AdminEntity adminEntity) {
    	try {
    		String user = adminEntity.getUserName();
    		AdminEntity foundUser = adminRepository.findByUserName(user);
    		String role = foundUser.getRoles().toString();
    		return role;
    		
    	}catch(Exception e) {
    		log.error("",e);
    	}
    	
    	return "Role not recognized";
        
    }
}
