package com.example.BankingSystem.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BankingSystem.entity.CustomerEntity;
import com.example.BankingSystem.repository.CustomerRepository;

@Service
public class EmployeeService {
	
	@Autowired
	private CustomerRepository customerRespository;
	
	public CustomerEntity createCustomer(CustomerEntity customer) {
		
		CustomerEntity customerSave = customerRespository.save(customer);
		return customerSave;
	}
	
	public Optional<CustomerEntity> getAccount(long accountNumber) {
		Optional<CustomerEntity> customerDetail = customerRespository.findById((long)accountNumber);
		if(customerDetail.isEmpty() && customerDetail.equals("")) {
			return customerDetail;
		}else {
			return null;
		}
	}
}
