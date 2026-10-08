package com.spring.controller;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.dto.ShortenUrlRequest;
import com.spring.dto.ShortenUrlResponse;
import com.spring.service.UrlShortnerService;
import com.spring.service.UrlShortnerServiceImpl;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;

@RestController
//@RequestMapping("/api/v1/url")
public class UrlController {

	private final UrlShortnerServiceImpl urlShortnerServiceImpl;
	
	public UrlController(UrlShortnerServiceImpl urlShortnerServiceImpl) {
		this.urlShortnerServiceImpl = urlShortnerServiceImpl;
	}
	
	@PostMapping("/api/v1/url/shorten")
	public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request) {
		
		 // We pass the URL from our request DTO to the service method we built earlier.
        // The service returns the unique, generated short code (e.g., "aB1cDe").
		String shortenUrl = urlShortnerServiceImpl.shortenUrl(request.url());
		
		// 2. Construct the full, user-facing short URL.
        // The service is only responsible for the code; the controller is responsible
        // for constructing the full URL based on the application's context.
        // For now, we will hardcode the base URL. In a real-world scenario, this would be
        // configured dynamically based on the environment.
		String fullShortUrl = "http://localhost:8080/"+shortenUrl;
		
		// 3. Create the response DTO.
        // We package the full short URL into our response object.
		ShortenUrlResponse response = new ShortenUrlResponse(fullShortUrl);
		
		// 4. Return the response wrapped in a ResponseEntity.
        // We explicitly set the HTTP status to 201 Created, which is the correct
        // semantic response for a successful POST request that creates a new resource.
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/{shortCode}")
	public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
		String originalUrl = urlShortnerServiceImpl.getOriginalUrlAndIncrementClicks(shortCode);
		
//		HttpHeaders header = new HttpHeaders();
//		header.setLocation(URI.create(originalUrl));
		
		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
	}
}
