package com.nitendrait.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nitendrait.entity.CountryEntity;

@Repository
public interface CountryRepo extends JpaRepository<CountryEntity, Integer> {

}
