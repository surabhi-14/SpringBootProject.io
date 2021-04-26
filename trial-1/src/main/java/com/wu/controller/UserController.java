package com.wu.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.wu.dao.CustomerRepository;
import com.wu.dao.MyOrderRepository;
import com.wu.helper.Message;
import com.wu.model.Customer;
import com.wu.model.MyOrder;
import com.wu.model.RazorPay;
import com.wu.model.Response;



@Controller
@RequestMapping("/user")
public class UserController {
	
	 //create razorpay variable.
    private RazorpayClient client;

    //for converting json and java objects.
    private static Gson gson = new Gson();

     //add your secretId and secretValue you got from your RazorPay account.
    private static final String SECRET_ID = "rzp_test_o2nYUPVkYmtY9i";
    private static final String SECRET_KEY = "WBTBY2DO7Idz6VHBL5gutXFB";
	
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private MyOrderRepository myOrderRepository;
	
	// method for adding common data to response
		@ModelAttribute
		public void addCommonData(Model model, Principal principal) {
			String userName = principal.getName();
	
			Customer customer = customerRepository.getCustomerByCustomerName(userName);

			model.addAttribute("user", customer);

		}
	
	@RequestMapping("/dashboard")
	public String dashboard(Model model) {
		model.addAttribute("title", "Payment Manager");
		return "normal/dashboard";
	} 
		
		 public UserController() throws RazorpayException {
		        this.client =  new RazorpayClient(SECRET_ID, SECRET_KEY);
		    }
		 
		 @RequestMapping(value="/index")
		    public String getHome() {
		        return "redirect:normal/paypage";
		    }
		    @RequestMapping(value="normal/paypage")
		    public String getHomeInit() {
		        return "normal/paypage";
		    }
		    
		    @RequestMapping(value="/createPayment", method=RequestMethod.POST)
		    @ResponseBody
		    public ResponseEntity<String> createOrder(@RequestBody Customer customer,Principal principal) {

		        try {
		             //creating an order in RazorPay.
		             //new order will have order id. you can get this order id by calling  order.get("id")
		            Order order = createRazorPayOrder( customer.getAmount() );
		            RazorPay razorPay = getRazorPay((String)order.get("id"), customer);
		            System.out.println(order); 
		            
		            MyOrder myOrder = new MyOrder();
		            myOrder.setAmount(order.get("amount")+ "");
		            myOrder.setMyOrderId(order.get("id"));
		            myOrder.setPaymentId(null);
		            myOrder.setStatus("created");
		            myOrder.setCustomer(this.customerRepository.getCustomerByCustomerName(principal.getName()));
		            myOrder.setReceipt(order.get("receipt"));
		            this.myOrderRepository.save(myOrder);
		 
		            
		            //200 - ok
		            return new ResponseEntity<String>(gson.toJson(getResponse(razorPay, 200)),
		                    HttpStatus.OK);
		        } catch (RazorpayException e) {
		            e.printStackTrace();
		        }
		        //500 - internal server error
		        return new ResponseEntity<String>(gson.toJson(getResponse(new RazorPay(), 500)),
		                HttpStatus.EXPECTATION_FAILED);
		    }

		    private Response getResponse(RazorPay razorPay, int statusCode) {
		        Response response = new Response();
		        response.setStatusCode(statusCode);
		        response.setRazorPay(razorPay);
		        
		        return response;
		    }

		    private RazorPay getRazorPay(String orderId, Customer customer) {
		    	
		    	
		        RazorPay razorPay = new RazorPay();
		        razorPay.setApplicationFee(convertRupeeToPaise(customer.getAmount()));
		        razorPay.setCustomerName(customer.getCustomerName());
		        razorPay.setCustomerEmail(customer.getEmail());
		        razorPay.setMerchantName("CollegeWork");
		        razorPay.setPurchaseDescription("Capture Payments");
		        razorPay.setRazorpayOrderId(orderId);
		        razorPay.setSecretKey(SECRET_ID);
		        razorPay.setImageURL("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.freepik.com%2Ffree-photos-vectors%2Fpayment&psig=AOvVaw1cY4TehtYFcmyGFQSCCQMv&ust=1617908563113000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCKi4spnp7O8CFQAAAAAdAAAAABAD");
		        razorPay.setTheme("#F37254");
		        razorPay.setNotes("notes"+orderId);

		        return razorPay;
		    }

		    private Order createRazorPayOrder(String amount) throws RazorpayException {

		        JSONObject options = new JSONObject();
		        options.put("amount", convertRupeeToPaise(amount));
		        options.put("currency", "INR");
		        options.put("receipt", "txn_123456");
		        options.put("payment_capture", 1); // You can enable this if you want to do Auto Capture.
		        return client.Orders.create(options);
		       
		    }

		    private String convertRupeeToPaise(String paise) {
		        BigDecimal b = new BigDecimal(paise);
		        BigDecimal value = b.multiply(new BigDecimal("100"));
		        return value.setScale(0, RoundingMode.UP).toString();

		    }
		    
		    @GetMapping("/show_history")
		    public String viewHistory(Model model,Principal principal) {
		    	model.addAttribute("title", "Payment Manager");
		    	/*String userName = principal.getName();
		    	Customer cust = this.customerRepository.getCustomerByCustomerName(userName);
		    	cust.getMyOrder();
		    	
		    	System.out.println(cust);*/
		    	
		    	String userName = principal.getName();
		    	Customer customer = this.customerRepository.getCustomerByCustomerName(userName);
		    	System.out.println(customer);
		    	List<MyOrder> myOrder = this.myOrderRepository.findHistoryByCustomer(customer.getId());
		    	System.out.println(myOrder);
		    	model.addAttribute("myOrder",myOrder);
		    	
		    	return "normal/show_history";
		    }
		    
		 // your profile handler
			@GetMapping("/profile")
			public String yourProfile(Model model) {
				model.addAttribute("title", "Payment Manager");
				return "normal/profile";
			}
			//open settings handler
			@GetMapping("/settings")
			public String openSettings() {
				return "normal/settings";
			}
			//change password handler
			@PostMapping("/changePassword")
			public String changePassword(@RequestParam("oldPassword")String oldPassword,@RequestParam("newPassword")String newPassword,Model model,Principal principal,HttpSession session) {
				model.addAttribute("title", "Payment Manager");
				String userName = principal.getName();
				
				 Customer cUser = this.customerRepository.getCustomerByCustomerName(userName);
				 if (this.bCryptPasswordEncoder.matches(oldPassword, cUser.getPassword())) {
					 cUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
					 this.customerRepository.save(cUser);
					 //not getting print
					 session.setAttribute("message", new Message("your password is updated","alert-success"));
				 }
				 else {
					 session.setAttribute("message", new Message("Please enter correct old password","alert-danger"));
					 return "normal/settings";
				 }
				return "normal/dashboard";
			}
		    
}
