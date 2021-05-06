package com.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
public class HomeControler {
    
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository; 
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title","Home- Smart Contact Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title","About- Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title","Register- Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	/* this handler is for register */
	
	@PostMapping("/do_register")
	public String registerUser(@Validated  @ModelAttribute("user")  User user,BindingResult bindingResult, @ RequestParam(value = "agreement", 
	defaultValue = "false") boolean agreement, Model model, 
			 HttpSession session) {
		
		try {
			
			if(!agreement) {
				System.out.println("You have not agreed terms and conditions");
				throw new Exception("You have not agreed terms and conditions");
			}
			
			if(bindingResult.hasErrors()) {
				System.out.println("Error-->"+bindingResult.toString());
				model.addAttribute("user",user);
				return "signup";
			}else {
				System.out.println("ddddd");
			}
			
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println("agreement-->"+agreement);
			System.out.println("user-->"+user);
			
		 User result =	this.userRepository.save(user);
		 model.addAttribute("user",result);
		 
		 session.setAttribute("message", new Message("SuccessFully register", "alert-success"));
		 return "signup";
		 
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went error" +e.getMessage(), "alert-danger"));
		}
		
		return "signup";
	}

//	handler for custom login
	@GetMapping("/signin")
	public String customLogin(Model model) {
		model.addAttribute("title","Login page");
		return "login";
	}
	
//	@Autowired
//	private UserRepository userRepository;
//	
//	@GetMapping("/test")
//	@ResponseBody
//	public String test() {
//		
//		User u  = new User();
//		u.setName("Vivek");
//		u.setEmail("vivek@gmail.com");
//		
//		userRepository.save(u);
//		
//		return "working";
//	}
}
