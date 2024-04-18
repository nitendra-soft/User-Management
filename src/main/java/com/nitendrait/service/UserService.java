package com.nitendrait.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.nitendrait.dto.LoginDto;
import com.nitendrait.dto.RegisterDto;
import com.nitendrait.dto.ResetPwdDto;
import com.nitendrait.dto.UserDto;
@Service
public interface UserService {
	
	
	// Get All Countries method
	public Map<Integer,String> getCountries();
	
	// Get state according to country id
	public Map<Integer,String> getStates(Integer cid);
	
	// Get City according to state id
	public Map<Integer,String> getCities(Integer sid);
	
	// Get user email 
	public UserDto getUser(String email);
	
	// Getting user registration details like registered user oor not
	public boolean registerUser(RegisterDto regDto);
	
	// Getting login details
	public UserDto getUser(LoginDto loginDto);
	
	// Method for getting password is updated or not
	public boolean resetPwd(ResetPwdDto pwdDto);
	
	// Method for third party api call
	public String getQuote();

}
