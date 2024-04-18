package com.nitendrait.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="STATE_MASTER")
public class StateEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer state_Id;
	private String state_Name;
	
	    @ManyToOne
	    @JoinColumn(name = "country_id")
	    private CountryEntity country;

	    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL)
	    private List<CityEntity> cities;

		public Integer getState_Id() {
			return state_Id;
		}

		public void setState_Id(Integer state_Id) {
			this.state_Id = state_Id;
		}

		public String getState_Name() {
			return state_Name;
		}

		public void setState_Name(String state_Name) {
			this.state_Name = state_Name;
		}

		public CountryEntity getCountry() {
			return country;
		}

		public void setCountry(CountryEntity country) {
			this.country = country;
		}

		public List<CityEntity> getCities() {
			return cities;
		}

		public void setCities(List<CityEntity> cities) {
			this.cities = cities;
		}

}
