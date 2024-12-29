package com.example.BankingSystem.controller;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BankingSystem.entity.AdminEntity;
import com.example.BankingSystem.entity.CustomerEntity;
import com.example.BankingSystem.service.AdminService;
import com.example.BankingSystem.service.CustomerService;
import com.example.BankingSystem.service.EmployeeService;
import com.example.BankingSystem.service.VerificationService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/customer")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private VerificationService verificationService;
	
	@PostMapping("/create-account")
	public ResponseEntity<?> createAccount(@RequestBody CustomerEntity customer) {
		verificationService.storeUserData(customer);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/generate-otp-email")
	public ResponseEntity<?> generateOtpEmail(@RequestBody CustomerEntity customer){
		try {
			verificationService.generateAndSendOTPEmail(customer.getEmail());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/generate-otp-phone")
	public ResponseEntity<?> generateOtpPhone(@RequestBody CustomerEntity customer){
		try {
			verificationService.generateAndSendOTPPhone(customer.getPhoneNumber());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/verify-email")
	public ResponseEntity<Boolean> verifyEmail(@RequestBody String email , @RequestBody String otp){
		boolean isVerified = verificationService.verifyAndSave(email, otp);
		System.out.println(email);
		System.out.println(otp);
		System.out.println(isVerified);
		if(isVerified) {
			return new ResponseEntity<>(true,HttpStatus.OK);
		}
		return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/verify-phone")
	public ResponseEntity<String> verifyPhone(@RequestBody String phoneNumber , @RequestBody String otp){
		boolean isVerified = verificationService.verifyAndSave(phoneNumber, otp);
		if(isVerified) {
			return new ResponseEntity<>("verified successful",HttpStatus.OK);
		}
		return new ResponseEntity<>("verified unsuccessful",HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping("/update-account/{accountNumber}")
	public void updateAccount(@RequestBody CustomerEntity customer , 
			@PathVariable long accountNumber) {
		
	}
	
	@PostMapping("/get-account")
	public ResponseEntity<?> getAccount( @RequestBody long accountNumber) {
		Optional<?> employeeDetail = employeeService.getAccount(accountNumber);
		
		if(employeeDetail != null) {
			return new ResponseEntity<>(employeeDetail,HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/deposite")
	public ResponseEntity<CustomerEntity> deposite(@RequestBody CustomerEntity customerEntity) {
		CustomerEntity accountDetails = customerService.deposit(customerEntity);
		if(accountDetails != null) {			
			return new ResponseEntity<>(accountDetails,HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}
	
	@PostMapping("/credit")
	public ResponseEntity<CustomerEntity> credit(@RequestBody CustomerEntity customerEntity) {
		CustomerEntity accountDetails = customerService.credit(customerEntity);
		if(accountDetails != null) {			
			return new ResponseEntity<>(accountDetails,HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/transfer")
	public ResponseEntity<Object> transfer(@RequestBody CustomerEntity customerEntity, @RequestBody long accountNumberTo) {
		CustomerEntity accountDetails = customerService.transfer(customerEntity, accountNumberTo);
		if(accountDetails != null) {			
			return new ResponseEntity<>(accountDetails,HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
