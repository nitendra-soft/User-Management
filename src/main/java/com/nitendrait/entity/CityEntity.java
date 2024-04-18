package com.nitendrait.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="CITY_MASTER")
public class CityEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer city_Id;
	private String city_Name;
	
	    @ManyToOne
	    @JoinColumn(name = "state_id")
	    private StateEntity state;

		public Integer getCity_Id() {
			return city_Id;
		}

		public void setCity_Id(Integer city_Id) {
			this.city_Id = city_Id;
		}

		public String getCity_Name() {
			return city_Name;
		}

		public void setCity_Name(String city_Name) {
			this.city_Name = city_Name;
		}

		public StateEntity getState() {
			return state;
		}

		public void setState(StateEntity state) {
			this.state = state;
		}

}
