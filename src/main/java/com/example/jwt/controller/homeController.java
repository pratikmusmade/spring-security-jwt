package com.example.jwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class homeController {
	@GetMapping("/")
	public String Test() {
		return "This is v1 Employee Controller";
	}
	
}
