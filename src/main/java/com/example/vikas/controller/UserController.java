package com.example.vikas.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.example.vikas.dao.ContactDao;
import com.example.vikas.dao.UserDao;
import com.example.vikas.entity.Contact;
import com.example.vikas.entity.User;
import com.example.vikas.helper.Message;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping(value="/user",method= {RequestMethod.GET,RequestMethod.POST})
public class UserController 
{
	
	
//sb handler mai chlega ye	
@ModelAttribute	
public void  commanvala(Principal principal,Model model)
{
	  User user = dao.getuserByUserName(principal.getName());
	  model.addAttribute("user",user);
	
}
	
@Autowired
private UserDao dao;

@Autowired
private ContactDao dao2;
	
	@GetMapping("/index")
	public String user(Principal principal,Model model)
	{
		model.addAttribute("title","USER_DASHBOARD");
	   
		return "normal/user_dashboard";
	}
	
	@GetMapping("/add_contact")
	public String addcontact(Model model)
	{
		model.addAttribute("title","ADD-CONTACT");
		model.addAttribute("contact",new Contact());
		return "normal/user_addcontact_form";
	}
	@PostMapping("/process_data")
	public String formdata(@RequestParam("name") String name,
			@RequestParam("secondName") String secondName,
			@RequestParam("phone") String phone,
			@RequestParam("email") String email,
			@RequestParam(defaultValue = "x",name = "image") MultipartFile file,
			Model model,Principal principal,HttpSession session
			)
	{
		
		
		
		try {
			Contact contact=new Contact();
			if(file==null)
			{
				throw new Exception("");
				
			}
			else
			{
				
				
				
				File saveFile = new ClassPathResource("static/image").getFile();
				
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				
				contact.setImage(file.getOriginalFilename());
				contact.setName(name);
				contact.setEmail(email);
				contact.setPhone(phone);
				contact.setSecondName(secondName);
				String name1 = principal.getName();
				User user = dao.getuserByUserName(name1);
				contact.setUser(user);
				user.getcontact().add(contact);
				System.out.println(file.getOriginalFilename());
				System.out.println(contact);
				dao.save(user);
				session.setAttribute("message", new Message("Added","alert-success"));
				return "normal/user_addcontact_form";
				
			}
		}
		catch(Exception e)

		{
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong","alert-danger"));
			return "normal/user_addcontact_form";
		}
		
		
	}

	
	@GetMapping("/show-contact/{page}")
	public String show(@PathVariable("page") int page,Model m,Principal principal)
	{
		
		String name = principal.getName();
		User getuserByUserName = dao.getuserByUserName(name);
		 Pageable pageable=  PageRequest.of(page,5);
	
		Page<Contact> contact = dao2.findById(getuserByUserName.getId(),pageable);
		m.addAttribute("contact",contact);
		m.addAttribute("title","View Page");
		m.addAttribute("currentpage",page);
		m.addAttribute("totalpage",contact.getTotalPages());
		
		return "normal/show_contact";
		
	}
	
	//delete the contact
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id,Principal principal)
	{
//		String name = principal.getName();
//		Optional<Contact> findById = dao2.findById(id);
//		Contact contact = findById.get();
		dao2.deleteById(id);
		return "redirect:/user/show-contact/0";
	}
	
	
	//update handler
	@PostMapping("/update-contact/{cId}")
	public String update(@PathVariable("cId") Integer id,Model m)
	{
		Contact contact = dao2.findById(id).get();
		m.addAttribute("contact",contact);
		return "/normal/update_contact";
	}
	
	
	@PostMapping("/updated_data/{cId}")
	public String updateddata(@PathVariable("cId") Integer id, @RequestParam("name") String name,
			@RequestParam("secondName") String secondName,
			@RequestParam("phone") String phone,
			@RequestParam("email") String email,
			@RequestParam(defaultValue = "x",name = "image") MultipartFile file,
			Model model)
	{
		Contact cc = dao2.findById(id).get();
		
		cc.setPhone(phone);
		cc.setName(name);
		cc.setSecondName(secondName);
		cc.setEmail(email);
		dao2.save(cc);
		
		
		return "redirect:/user/show-contact/0";
	}
}
