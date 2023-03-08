package com.example.vikas.controller;

import java.sql.SQLIntegrityConstraintViolationException;

import javax.validation.Valid;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.vikas.dao.UserDao;
import com.example.vikas.entity.User;
import com.example.vikas.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class MyController 
{
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserDao userdao;
	
	
	@GetMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title","Home Page");
	
		return "index";
	}
	@GetMapping("/about")
	public String about(Model model)
	{
		System.out.println("vikas verma no");
		model.addAttribute("title","About Page");
	
		return "about";
	}
	@GetMapping("/signup")
	public String signup(Model model)
	{
		model.addAttribute("user",new User());

		model.addAttribute("title","SignUp Page");
	
		return "signup";
	}
	
	@PostMapping("/do_reg")
	public String register(@Valid@ModelAttribute("user") User user,BindingResult result1,
			@RequestParam(value="check",defaultValue="false") boolean check,
			@RequestParam("confirmPassword") String pass,Model model,
			HttpSession session
			)
	{
		
		try {
			System.out.println(user);
			
			if(result1.hasErrors())
			{
				System.out.println(result1);
				model.addAttribute(user);
				return "signup";
			}
			if(!check&&user.getPassword()!=pass)
			{
				throw new Exception("You have not tick term and condition");
				 
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user.setImageUrl("iamge.png");
			
			//we simply put this into data base
			userdao.save(user);
			
			//we send blank user
			model.addAttribute("user",new User());
			session.setAttribute("message",
					new Message("SuccessFully done!!",
							"alert-success") );
			return "signup";
			
			
		}
		catch(Exception e)
		{
			 if(e.getCause() != null && e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
			        SQLIntegrityConstraintViolationException sql_violation_exception = (SQLIntegrityConstraintViolationException) e.getCause().getCause() ;
			        session.setAttribute("message",
							new Message("An account with same email already exist",
									"alert-danger") );
			        return "signup";
			    }
			
			e.printStackTrace();
			System.out.println(e);
			model.addAttribute("user",user);
			session.setAttribute("message",
					new Message("Something went wrong!!",
							"alert-danger") );
			return "signup";
			
			
		}
	
	}
	
	@GetMapping("/signin")
	public String login(Model model)
	{
		model.addAttribute("title","LoginPAGE");
		return "login";
	}

	@PostMapping("/signin")
	public void loginDone()
	{
		System.out.println("vikas verma post vala signin");
		
		
		
	}
}
