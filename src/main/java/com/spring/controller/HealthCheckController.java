package com.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/ping")
public class HealthCheckController {

	@GetMapping("/ping")
	public String ping() {
		return "PONG!";
	}
}
