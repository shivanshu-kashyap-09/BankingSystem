package com.example.BankingSystem.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BankingSystem.entity.AdminEntity;
import com.example.BankingSystem.service.AdminService;
import com.example.BankingSystem.service.VerificationService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired 
	private VerificationService verificationService;
	
	@PostMapping("/create-admin")
	public ResponseEntity<?> createAdmin(@RequestBody AdminEntity adminEntity) {
		try {
			verificationService.storeUserData(adminEntity);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping("/create-employee")
	public ResponseEntity<?> createEmployee(@RequestBody AdminEntity adminEntity) {
		adminEntity.setRoles(Arrays.asList("EMPLOYEE"));
		verificationService.storeUserData(adminEntity);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<?> getAll() {
		List<AdminEntity> all = adminService.getAll();
		return new ResponseEntity<>(all,HttpStatus.OK);
	}
	
	@PostMapping("/generate-otp-email")
	public ResponseEntity<?> generateOtpEmail(@RequestBody AdminEntity adminEntity){
		try {
			verificationService.generateAndSendOTPEmail(adminEntity.getEmail());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/generate-otp-phone")
	public ResponseEntity<?> generateOtpPhone(@RequestBody AdminEntity adminEntity){
		try {
			verificationService.generateAndSendOTPPhone(adminEntity.getPhoneNumber());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/verify-email")
	public ResponseEntity<String> verifyEmail(@RequestBody Map<String, String> payload){
		String email = payload.get("email");
	    String otp = payload.get("otp");

	    if (email == null || otp == null) {
	        return ResponseEntity.badRequest().body("Email and OTP are required.");
	    }

		boolean isVerified = verificationService.verifyAndSave(email, otp);
	    if (isVerified) {
	        return ResponseEntity.ok("Email verified successfully.");
	    } else {
	        return ResponseEntity.badRequest().body("Invalid email or OTP.");
	    }
	}
	
	@PostMapping("/verify-phone")
	public ResponseEntity<String> verifyPhone(@RequestBody Map<String, String> payload){
		String phoneNumber = payload.get("phoneNumber");
	    String otp = payload.get("otp");

	    if (phoneNumber == null || otp == null) {
	        return ResponseEntity.badRequest().body("phoneNumber and OTP are required.");
	    }

		boolean isVerified = verificationService.verifyAndSave(phoneNumber, otp);
	    if (isVerified) {
	        return ResponseEntity.ok("phoneNumber verified successfully.");
	    } else {
	        return ResponseEntity.badRequest().body("Invalid phoneNumber or OTP.");
	    }
	}
	
	@PostMapping("/forget-password-otp-email")
	public ResponseEntity<Object> forgetPasswordOtpEmail(@RequestBody String email) {
		boolean data = adminService.findEmail(email);
		if(data) {
			try {
				verificationService.generateAndSendOTPEmail(email);
				return new ResponseEntity<>(HttpStatus.OK);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	String email;
	
	@PostMapping("/forget-password-verify-email")
	public ResponseEntity<String> verifyForgetPaswordEmail(@RequestBody Map<String, String> payload) {
		email = payload.get("email");
	    String otp = payload.get("otp");

	    if (email == null || otp == null) {
	        return ResponseEntity.badRequest().body("Email and OTP are required.");
	    }

		boolean isVerified = verificationService.verifyForgetPassword(email, otp);
		
	    if (isVerified) {
	        return ResponseEntity.ok("Email verified successfully.");
	    } else {
	        return ResponseEntity.badRequest().body("Invalid email or OTP.");
	    }
	}
	
	@PostMapping("/forget-password-email")
	public ResponseEntity<Object> forgetPasswordEmail(@RequestBody Map<String, String> payload) {
		String newPassword = payload.get("newPassword");
		String confirmPassword = payload.get("confirmPassword");
		
		ResponseEntity<Object> forget  = verificationService.forgetPassword(email, newPassword, confirmPassword);
		return forget;
	}
	
	@PostMapping("/forget-password-otp-phone")
	public ResponseEntity<Object> forgetPasswordOtpPhone(@RequestBody String phoneNumber) {
		boolean data = adminService.findPhoneNumber(phoneNumber);
		if(data) {
			try {
				verificationService.generateAndSendOTPPhone(phoneNumber);
				return new ResponseEntity<>(HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	String phoneNumber;
	
	@PostMapping("/forget-password-verify-phone")
	public ResponseEntity<String> verifyForgetPaswordPhone(@RequestBody Map<String, String> payload) {
		phoneNumber = payload.get("phoneNumber");
		String otp = payload.get("otp");
		
		if (phoneNumber == null || otp == null) {
			return ResponseEntity.badRequest().body("phone number and OTP are required.");
		}
		
		boolean isVerified = verificationService.verifyForgetPassword(phoneNumber, otp);
		
		if (isVerified) {
			return ResponseEntity.ok("phone number verified successfully.");
		} else {
			return ResponseEntity.badRequest().body("Invalid phone number or OTP.");
		}
	}
	
	@PostMapping("/forget-password-phone")
	public ResponseEntity<Object> forgetPasswordPhone(@RequestBody Map<String, String> payload) {
		String newPassword = payload.get("newPassword");
		String confirmPassword = payload.get("confirmPassword");
		
		ResponseEntity<Object> forget  = verificationService.forgetPassword(phoneNumber, newPassword, confirmPassword);
		return forget;
	}
}
