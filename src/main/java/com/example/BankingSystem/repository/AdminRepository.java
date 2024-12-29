package com.example.BankingSystem.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.BankingSystem.entity.AdminEntity;

@Repository
public interface AdminRepository extends MongoRepository<AdminEntity,ObjectId> {

	AdminEntity findByUserName(String userName);
	
	AdminEntity findByEmail(String email);
	
	AdminEntity findByPhoneNumber(String phoneNumber);

	
}
