package com.spring.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotEmpty;

public record ShortenUrlRequest(
		@NotEmpty(message = "URL cannot be empty")
		@URL(message = "A valid URL format is required")
		String url	
) {
}
