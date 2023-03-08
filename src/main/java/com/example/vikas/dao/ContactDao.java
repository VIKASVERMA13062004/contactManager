package com.example.vikas.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.vikas.entity.Contact;

public interface ContactDao extends CrudRepository<Contact, Integer> 
{
@Query("from Contact as c where c.user.id=:userid")
Page<Contact> findById(@Param("userid") int userId,Pageable pageable);
}
