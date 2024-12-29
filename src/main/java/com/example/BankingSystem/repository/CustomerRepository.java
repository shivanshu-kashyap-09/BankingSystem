package com.example.BankingSystem.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import com.example.BankingSystem.entity.CustomerEntity;

@EnableMongoRepositories
@Repository
public interface CustomerRepository extends MongoRepository<CustomerEntity,Long> {

}
