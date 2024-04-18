package com.nitendrait.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nitendrait.dto.LoginDto;
import com.nitendrait.dto.QuoteDto;
import com.nitendrait.dto.RegisterDto;
import com.nitendrait.dto.ResetPwdDto;
import com.nitendrait.dto.UserDto;
import com.nitendrait.entity.CityEntity;
import com.nitendrait.entity.CountryEntity;
import com.nitendrait.entity.StateEntity;
import com.nitendrait.entity.UserDtlsEntity;
import com.nitendrait.repo.CityRepo;
import com.nitendrait.repo.CountryRepo;
import com.nitendrait.repo.StateRepo;
import com.nitendrait.repo.UserDtlsRepo;
import com.nitendrait.util.EmailUtils;
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDtlsRepo userDtlsRepo;
	
	@Autowired
	private CountryRepo countryRepo;
	
	@Autowired
	private StateRepo stateRepo;
	
	@Autowired
	private CityRepo cityRepo;
	@Autowired
	private EmailUtils emailUtils;
	
	private QuoteDto[] quotetions =null;
	

	@Override
	public Map<Integer, String> getCountries() {
		
		HashMap<Integer,String> countryMap=new HashMap<>();
		List<CountryEntity> countriesList = countryRepo.findAll();
		countriesList.forEach(c ->{
			countryMap.put(c.getCountry_Id(),c.getCountry_Name() );
		});
		return countryMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer cid) {
		
		HashMap<Integer,String> stateMap=new HashMap<>();
		/*
		CountryEntity country=new CountryEntity();
		country.setCountry_Id(cid);
		
		StateEntity entity=new StateEntity();
		entity.setCountry(country);
		
		Example<StateEntity> of = Example.of(entity);
		List<StateEntity> stateList = stateRepo.findAll(of);
		*/
		
		List<StateEntity> stateList =stateRepo.getStates(cid);
		stateList.forEach(s->{
			stateMap.put(s.getState_Id(), s.getState_Name());
		});
		
		return stateMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer sid) {
		
		HashMap<Integer,String> citiesMap=new HashMap<>();
		List<CityEntity> citiesList=cityRepo.getCities(sid);
		citiesList.forEach(c->{
			citiesMap.put(c.getCity_Id(), c.getCity_Name());
		});
		return citiesMap;
	}

	@Override
	public UserDto getUser(String email) {
		UserDtlsEntity userDtlsEntity = userDtlsRepo.findByEmail(email);
		/*
		UserDto dto=new UserDto();
		BeanUtils.copyProperties(userDtlsEntity, dto);
		*/
		if(userDtlsEntity==null) {
			return null;
		}
		
		ModelMapper mapper=new ModelMapper();
		UserDto userDto = mapper.map(userDtlsEntity, UserDto.class);
		
		return userDto;
	}

	@Override
	public boolean registerUser(RegisterDto regDto) {
		
		ModelMapper mapper=new ModelMapper();
		
		UserDtlsEntity userDtlsEntity = mapper.map(regDto, UserDtlsEntity.class);
		
		CountryEntity country = countryRepo.findById(regDto.getCountryId()).orElseThrow();
		StateEntity state = stateRepo.findById(regDto.getStateId()).orElseThrow();
		CityEntity city = cityRepo.findById(regDto.getCityId()).orElseThrow();
		
		userDtlsEntity.setCountry(country);
		userDtlsEntity.setState(state);
		userDtlsEntity.setCity(city);
		userDtlsEntity.setPwd(generateRandom());
		userDtlsEntity.setPwdUpdated("No");
		
		//user Registration
		UserDtlsEntity savedEntity = userDtlsRepo.save(userDtlsEntity);
		
		String subject="User Registration";
		String body="Your Temporary Pwd is "+userDtlsEntity.getPwd();
		
		emailUtils.sendEmail(regDto.getEmail(), subject, body);
		
		return savedEntity.getUserId()!=null;
	}

	@Override
	public UserDto getUser(LoginDto loginDto) {
		
		UserDtlsEntity userDtlsEntity = userDtlsRepo.findByEmailAndPwd(loginDto.getEmail(), loginDto.getPwd());
		if(userDtlsEntity==null) {
			return null;
		}
		ModelMapper mapper=new ModelMapper();
	return mapper.map(userDtlsEntity, UserDto.class);
		
	}

	@Override
	public boolean resetPwd(ResetPwdDto pwdDto) {
		UserDtlsEntity byEmailAndPwd = userDtlsRepo.findByEmailAndPwd(pwdDto.getEmail(), pwdDto.getOldPwd());
		if(byEmailAndPwd!=null) {
			byEmailAndPwd.setPwd(pwdDto.getNewPwd());
			byEmailAndPwd.setPwdUpdated("Yes");
			userDtlsRepo.save(byEmailAndPwd);
			return true;
		}
		return false;
	}

	@Override
	public String getQuote() {
		
		if(quotetions==null) {
			
			String url="https://type.fit/api/quotes";
			//web Service call
			RestTemplate rt=new RestTemplate();
			ResponseEntity<String> forEntity = rt.getForEntity(url, String.class);
			String responseBody = forEntity.getBody();
			
			ObjectMapper objectMapper=new ObjectMapper();
			try {
			quotetions = objectMapper.readValue(responseBody, QuoteDto[].class);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
		
		
		Random r=new Random();
		int index = r.nextInt(quotetions.length-1);
		return quotetions[index].getText();
	}
	
	private static String generateRandom() {
		String aToZ="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	    Random rand=new Random();
	    StringBuilder res=new StringBuilder();
	    for (int i = 0; i < 5; i++) {
	       int randIndex=rand.nextInt(aToZ.length()); 
	       res.append(aToZ.charAt(randIndex));            
	    }
	    return res.toString();
	}

}
