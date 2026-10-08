package com.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.service.UrlShortnerServiceImpl;

@Controller
public class PageController {
	
	private final UrlShortnerServiceImpl urlServiceImpl;
	
	public PageController(UrlShortnerServiceImpl urlServiceImpl) {
		this.urlServiceImpl = urlServiceImpl;
	}

	@GetMapping("/")
	public String indexPage() {
		return "index";
	}
	
	@PostMapping("/shorten-web")
	public String handleShortenForm(@RequestParam("longUrl") String longUrl , Model model) {
		
		String shortCode = urlServiceImpl.shortenUrl(longUrl);
		
		String fullShortCode = "http://localhost:8080/"+shortCode;
		
		model.addAttribute("originalUrl", longUrl);
		model.addAttribute("shortUrlResult", fullShortCode);
		
		return "index";
	}
}
