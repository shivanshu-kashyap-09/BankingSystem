package com.example.BankingSystem.entity;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "customer")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {
	
	
	@Id
	private long accountNumber;
	
	@NonNull
	private String name;
	
	private String email;
	
	@NonNull
	private String phoneNumber;
	
	private long balance;
	
	@NonNull
	private int securityPin;
	
	private boolean isVerified;
	
	private String roles = "CUSTOMER";

}
