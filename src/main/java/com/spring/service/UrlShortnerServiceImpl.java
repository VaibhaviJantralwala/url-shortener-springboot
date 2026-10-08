package com.spring.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.UrlMappingRepository;
import com.spring.model.UrlMapping;

import jakarta.transaction.Transactional;

@Service
public class UrlShortnerServiceImpl implements UrlShortnerService{
	
		private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		// di using constrcutor injection
		private final UrlMappingRepository urlMapRepo;
		
		// constructor injection
		public UrlShortnerServiceImpl(UrlMappingRepository urlMapRepo) {
			this.urlMapRepo = urlMapRepo;
		}
		
		@Override
		@Transactional
		public String shortenUrl(String originalUrl) {
			 // 1. Create a placeholder entity
			UrlMapping url = new UrlMapping();
			url.setOriginalUrl(originalUrl);
			url.setCreationTime(LocalDateTime.now());
			
			 // 2. First save to generate the unique ID
			UrlMapping savedEntity = urlMapRepo.save(url);
			
			// 3. Convert the ID to a base-62 short code
			String shortCode = encodeBase62(savedEntity.getId());
			
			// 4. Update the entity with the generated code
			savedEntity.setShortCode(shortCode);
			
			// 5. Second save to persist the short code in the database
			urlMapRepo.save(savedEntity);
			
			// 6. Fulfill the method's contract by returning the generated short code.
	        // This value is what the caller (e.g., our future API controller) will receive
	        // and use to construct the final, shareable short URL for the user.
			return shortCode;
		}

		private String encodeBase62(Long number) {

			if(number == 0) {
				return String.valueOf(BASE62_CHARS.charAt(0));
			}
			
			StringBuilder sb = new StringBuilder();
			long num = number;
			
			while(num > 0) {
				int remainder =(int)(num% 62);
				sb.append(BASE62_CHARS.charAt(remainder));
				num /= 62;
			}
			
			return sb.reverse().toString();
		}

		@Override
		@Transactional
		public String getOriginalUrlAndIncrementClicks(String shortenUrl) {
			
			// Here, we use the custom query method we defined in our repository.
	        // Spring Data JPA implements this method for us based on its name.
	        // It executes a query to find a UrlMapping entity where the 'shortCode' column
	        // matches the value passed to the method.
	        // The result is wrapped in an Optional, which is a container that may or may not
	        // hold a value. This is a robust way to handle cases where the short code might not exist.
			
			Optional<UrlMapping> urlMapping = urlMapRepo.findByShortCode(shortenUrl);
			
			if( urlMapping.isPresent() ) {
				UrlMapping url = urlMapping.get();
				if( url.getClickCount() == null ) {
					url.setClickCount(1L);
				}else {
				url.setClickCount(url.getClickCount()+1);
				}
				urlMapRepo.save(url);
				
				return url.getOriginalUrl();
			}
			return null;
		}
		
		
		
}
