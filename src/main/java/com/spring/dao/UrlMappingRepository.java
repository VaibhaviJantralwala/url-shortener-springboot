package com.spring.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.model.UrlMapping;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long>{

	Optional<UrlMapping> findByShortCode(String shortCode);

}
