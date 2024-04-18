package com.nitendrait.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="COUNTRY_MASTER")
public class CountryEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer country_Id;
	private String country_Name;
	
	 @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
	  private List<StateEntity> states;

	public Integer getCountry_Id() {
		return country_Id;
	}

	public void setCountry_Id(Integer country_Id) {
		this.country_Id = country_Id;
	}

	public String getCountry_Name() {
		return country_Name;
	}

	public void setCountry_Name(String country_Name) {
		this.country_Name = country_Name;
	}

	public List<StateEntity> getStates() {
		return states;
	}

	public void setStates(List<StateEntity> states) {
		this.states = states;
	}

}
