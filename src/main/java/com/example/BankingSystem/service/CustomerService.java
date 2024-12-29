package com.example.BankingSystem.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.BankingSystem.entity.CustomerEntity;
import com.example.BankingSystem.repository.CustomerRepository;

@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Transactional
    public CustomerEntity deposit(CustomerEntity customerEntity) {
    	long accountNumber = customerEntity.getAccountNumber();
    	System.out.println(accountNumber);
    	Optional<CustomerEntity> account = customerRepository.findById((long)accountNumber);
        System.out.println(account);
        
        if (account.isPresent()) {
            CustomerEntity accountDetails = account.get();
            long Pin = accountDetails.getSecurityPin();
            
            if (Pin == customerEntity.getSecurityPin()) {
                
                long balance = accountDetails.getBalance() + customerEntity.getBalance();
                accountDetails.setBalance(balance);
                
                customerRepository.save(accountDetails);
                
                return accountDetails;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    
    @Transactional
    public CustomerEntity credit(CustomerEntity customerEntity) {
    	Optional<CustomerEntity> account = customerRepository.findById((long)customerEntity.getAccountNumber());
    	
    	if (account.isPresent()) {
    		CustomerEntity accountDetails = account.get();
    		long Pin = accountDetails.getSecurityPin();
    		
    		if (Pin == customerEntity.getSecurityPin()) {
    			
    			long balance = accountDetails.getBalance() - customerEntity.getBalance();
    			accountDetails.setBalance(balance);
    			
    			customerRepository.save(accountDetails);
    			return accountDetails;
    		} else {
    			return null;
    		}
    	} else {
    		return null;
    	}
    }
    
    @Transactional
    public CustomerEntity transfer(CustomerEntity customerEntity, long accountNumberTo) {
    	Optional<CustomerEntity> accountFrom = customerRepository.findById((long)customerEntity.getAccountNumber());
    	Optional<CustomerEntity> accountTo = customerRepository.findById(accountNumberTo);
    	
    	System.out.println(accountFrom);
    	System.out.println(accountTo);
    	
    	if (accountFrom.isPresent() && accountTo.isPresent()) {
    		CustomerEntity accountDetailsFrom = accountFrom.get();
    		CustomerEntity accountDetailsTo = accountTo.get();
    		long Pin = accountDetailsFrom.getSecurityPin();
    		
    		if (Pin == customerEntity.getSecurityPin()) {
    			
    			long balance = accountDetailsFrom.getBalance() - customerEntity.getBalance();
    			long addBalance = accountDetailsTo.getBalance() + balance;
    			
    			accountDetailsFrom.setBalance(balance);
    			accountDetailsTo.setBalance(addBalance);
    			
    			customerRepository.save(accountDetailsFrom);
    			customerRepository.save(accountDetailsTo);
    			return accountDetailsFrom;
    		} else {
    			throw new IllegalArgumentException("Invalid security pin.");
    		}
    	} else {
    		throw new IllegalArgumentException("Account not found.");
    	}
    }
}
