package com.wu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Trial1Application {

	public static void main(String[] args) {
		SpringApplication.run(Trial1Application.class, args);
	}
	public static String start() {
		System.out.println("start");
		return "start";
	}
	
	public static String stop() {
		System.out.println("stop");
		return "stop";
	}
}
