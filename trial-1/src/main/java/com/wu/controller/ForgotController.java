package com.wu.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wu.dao.CustomerRepository;
import com.wu.model.Customer;
import com.wu.service.EmailService;

@Controller
public class ForgotController {
	Random random = new Random(1000);
	@Autowired
	private EmailService emailService;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//email id form open handler
	@RequestMapping("/forgot")
	public String openEmailForm() {
		return "forgot_email_form";
	}
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("username") String email,HttpSession session) {
		System.out.println("email"+ email);
		//generating 4 digit otp
		
		int otp = random.nextInt(999999);
		System.out.println("OTP"+ otp);
		
		//code for sent otp to email
		
		String subject ="OTP from CollegeProject";
		String message =""
				+"<div style='border:1px solid #e2e2e2,padding:20px'>"
				+"<h1>"
				+"OTP is"
				+"<b>"+ otp 
				+"</b>"
				+"</h1>"
				+"</div>";
		String to =email;
		boolean flag = this.emailService.sendEmail(subject, message, to);
		
		if(flag) {
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			return "verify_otp";
		}
		else {
			session.setAttribute("message", "check your email id");
			return "forgot_email_form";
		}
	}
	
	//verify handler
	@PostMapping("/verify-otp")
	public String verifyotp(@RequestParam("otp") int otp , HttpSession session ) {
		int myOtp = (int)session.getAttribute("myotp");
		String email = (String)session.getAttribute("email");
		if(myOtp==otp) {
			//password change form
			Customer cust = this.customerRepository.getCustomerByCustomerName(email);
			if(cust==null) {
				//send error msg
				session.setAttribute("message", "user does not exist");
				return "forgot_email_form";
		
			}else {
				//send change password form
				return "password_change_form";
			}
			
		}else {
			session.setAttribute("message", "You have eneterd wrong otp");
			return "verify_otp";
		}
	}
	//change password
	@PostMapping("/changepassword")
	public String changePassword(@RequestParam("newpassword")String changepassword,HttpSession session) {
		String email;
		email = (String)session.getAttribute("email");
		Customer cust = this.customerRepository.getCustomerByCustomerName(email);
		cust.setPassword(this.bCryptPasswordEncoder.encode("newpassword"));
		this.customerRepository.save(cust);
		session.setAttribute("message", "Change successfull");
		return "login";
	}
}
