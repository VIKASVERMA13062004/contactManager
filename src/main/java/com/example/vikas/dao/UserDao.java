package com.example.vikas.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.example.vikas.entity.User;
@Component
public interface UserDao extends CrudRepository<User, Integer> 
{
	@Query("select u from User u where u.email= :email")
     public User getuserByUserName(@Param("email") String email);
}
