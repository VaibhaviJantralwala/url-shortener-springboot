package com.spring.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotEmpty;

public record ShortenUrlResponse(String shortUrl) {}
