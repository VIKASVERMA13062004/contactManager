package com.example.vikas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.vikas.dao.UserDao;
import com.example.vikas.entity.User;

public class UserDetailsServiceImpl implements UserDetailsService 
{
	@Autowired
    private	UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.getuserByUserName(username);
		if(user==null)
		{
			throw new UsernameNotFoundException("");
		}
		CustomUserDetails customUserDetails=new CustomUserDetails(user);
		
		return customUserDetails;
	}

}
