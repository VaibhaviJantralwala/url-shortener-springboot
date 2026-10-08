package com.spring.service;

public interface UrlShortnerService {
	
	public String shortenUrl(String originalUrl);
	
	public String getOriginalUrlAndIncrementClicks(String shortenUrl);
}
