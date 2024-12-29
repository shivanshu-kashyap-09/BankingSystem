package com.example.BankingSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BankingSystem.entity.AdminEntity;
import com.example.BankingSystem.repository.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    
    public List<AdminEntity> getAll() {
        return adminRepository.findAll();
    }
    
    public boolean findEmail(String email) {
    	AdminEntity data = adminRepository.findByEmail(email);
    	
    	if(data != null) {
    		return true;
    	}
    	return false;
    }
    
    public boolean findPhoneNumber(String phoneNumber) {
    	AdminEntity data = adminRepository.findByPhoneNumber(phoneNumber);
    	
    	if(data != null) {
    		return true;
    	}
    	return false;
    }
}
