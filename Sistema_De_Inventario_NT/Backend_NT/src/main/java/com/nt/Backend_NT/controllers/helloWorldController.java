package com.nt.Backend_NT.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class helloWorldController {
	
	@GetMapping
	public String doHelloWorld() {
		return "Hello world from a good spring boot project";
	}

}
