package com.nitendrait.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nitendrait.entity.CityEntity;

@Repository
public interface CityRepo extends JpaRepository<CityEntity, Integer> {
	
	@Query(value="Select * from CITY_MASTER where state_id=:stateId", nativeQuery=true)
	public List<CityEntity> getCities(Integer stateId);

}
