package com.example.BankingSystem.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminEntity {
	
	@Id
	private ObjectId id;
	
	@NonNull
	private String userName;
	
	@NonNull
	private String password;
	
	private String email;
	
	private String phoneNumber;
	
	private String otp;
	
	private LocalDateTime otpGenerateTime;
	
	private boolean verified;
	
	private List<String> roles;
	

}
