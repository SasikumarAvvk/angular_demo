package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
	
	@Query("from Users where emailId = :emailId and password = :password")
	Users userLogin(@Param("emailId") String emailId, @Param("password") String password);
    
	Users findByEmailId(String emailId);
}
