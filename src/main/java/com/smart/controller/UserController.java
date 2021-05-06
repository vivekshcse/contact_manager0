package com.smart.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.sun.xml.bind.api.impl.NameConverter.Standard;

@Controller
@RequestMapping("/user")
public class UserController {

	 @Autowired
	 private UserRepository userRepository; 
	 @Autowired
	 private ContactRepository contactRepository;
	
//	 this method for add common data in to model this will call autometic 
	 @ModelAttribute
	 public void addCommonData(Model model, Principal principal) {
//		 System.out.println(principal.getName()); 
		  String userName = principal.getName();
		  User user = userRepository.getUserByUserName(userName); 
		  model.addAttribute("user",user);
	 }
	 
//	  dashboard home
	  @RequestMapping("/index")
	  public String dashboard(Model model, Principal principal) {
		 return "normal/user_dashboard";
	  }
	  
	  
//	  handler to add contact
	  @GetMapping("/add-contact")
	  public String addOpenContactForm(Model model) {
		  model.addAttribute("title","Add Contact");
		  model.addAttribute("contact",new Contact());
		  return "normal/add_contact_form";
	  }
	  
//	  save contact data in db
	  @PostMapping("/process-contact")
	  public String  processContact(
			  @ModelAttribute Contact contact,
			  @RequestParam("profileImage") MultipartFile file, 
			  Principal principal,
			  HttpSession session) {
		 
             try {
				  String name = principal.getName();
				  System.out.println("Name-->"+name);
				  User user = this.userRepository.getUserByUserName(name);
				 
				  
				 
//				  processing and uploading
				  if(file.isEmpty()) {
//					  if the file is empty then try message
				  }else{
//					  upload the file to folder and update the name to contact
					  contact.setImage(file.getOriginalFilename());
					  
					  File saveFile = new ClassPathResource("static/img").getFile();
					  
					  Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
					  Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				  }
				  
				  contact.setUser(user);
				  user.getContacts().add(contact);
				  System.out.println("hello ho");
				  this.userRepository.save(user);
				  System.out.println("Data -->"+contact);
				  
				  System.out.println("Added dasta to base");
				  
//				  Message sucess
				  session.setAttribute("message", new Message("Your Content is added !! Add more", "success"));
             }catch(Exception e) {
//            	  Message Sucess
            	  session.setAttribute("message", new Message("Something went wrong try again..", "danger"));
            	 
            	 System.out.println(e.getMessage());
             }
		          return "normal/add_contact_form";
		 }
	  
//	  show contact handler
	  @GetMapping("/show-contacts/{page}")
	  public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {  
		  model.addAttribute("title","View-Contacts");
		  
		  String userName = principal.getName();
		  User user = this.userRepository.getUserByUserName(userName);
		  
		  
		  Pageable pageable = PageRequest.of(page, 5);
		  Page<Contact> contacts =  this.contactRepository.findContactsByUser(user.getId(),pageable);
		  
		  
		  model.addAttribute("contacts",contacts);
		  model.addAttribute("currentPage",page);
		  model.addAttribute("totalPages",contacts.getTotalPages());
		  
		  return "normal/show-contacts";
	  }
	  
	} 
	  
	  






