package com.wu.controller;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import com.wu.dao.CustomerRepository;
import com.wu.helper.Message;
import com.wu.model.Customer;




@Controller
public class HomeController {
	
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private CustomerRepository customerRepository;

	//handler for home
	@RequestMapping("/")
	public String home(Model model) 
	{
		model.addAttribute("title","Payment Manager");
		return "home";
	}
	//handler for about
	@RequestMapping("/about")
	public String about(Model model) 
	{
		model.addAttribute("title","Payment Manager");
		return "about";
	}
	
	//handler for signup
	@RequestMapping("/signup")
	public String signup(Model model) 
	{
		model.addAttribute("title","Payment Manager");
		model.addAttribute("customer",new Customer());
		return "signup";
	}
	
	//handler for registering customer
	@RequestMapping(value="/do_register",method=RequestMethod.POST)
	public String registerUser(@ Valid @ModelAttribute("customer") Customer customer, BindingResult result1,@RequestParam(value="agreement",defaultValue="false") boolean agreement,Model model,HttpSession session ) {
	
		
		try {
			if(!agreement) {
				System.out.println("you have not agreed terms and codntions");
				throw new Exception("you have not agreed terms and codntions");
			}
			
			if(result1.hasErrors()) {
				
				System.out.println("ERROR"+result1.toString());
				model.addAttribute("customer", customer);
				return "signup";
			}
			
		customer.setRole("ROLE_USER");
		customer.setPassword(passwordEncoder.encode(customer.getPassword()));
			
		System.out.println("Agreement" +agreement);	
		System.out.println("CUSTOMER" +customer);
		
		Customer result = this.customerRepository.save(customer);
		
		model.addAttribute("user",new Customer());
		
		session.setAttribute("message", new Message("Successfully Registered ","alert-success"));
		
		return "signup";
		
		} catch(Exception e){
		e.printStackTrace();
		model.addAttribute("customer",customer);
		session.setAttribute("message", new Message("Something Went Wrong " + e.getMessage(),"alert-danger"));
		
		return "signup";
		}
	
	}

	//handler for custom login
	@GetMapping("/signin")
	public String customLogin(Model model) {
		
		model.addAttribute("title", "Payment Manager");
		return "login";
	}
	
}
