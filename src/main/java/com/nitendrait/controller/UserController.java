package com.nitendrait.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nitendrait.dto.LoginDto;
import com.nitendrait.dto.RegisterDto;
import com.nitendrait.dto.ResetPwdDto;
import com.nitendrait.dto.UserDto;
import com.nitendrait.service.UserService;
import com.nitendrait.util.AppConstants;
import com.nitendrait.util.AppProperties;
@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AppProperties props;
	
	// method for register page  loading
	@GetMapping("/register")
	public String registerPage(Model model) {
		
		model.addAttribute("registerDto", new RegisterDto());
		model.addAttribute("countries", userService.getCountries());
		
		return "registerView";
	}
	
	//method for loading state
	@GetMapping("/states/{cid}")
	@ResponseBody
	public Map<Integer, String> getStates(@PathVariable("cid") Integer cid){
		
		return userService.getStates(cid);
		
	}
	
	// method for getting city
	@GetMapping("/cities/{sid}")
	@ResponseBody
	public Map<Integer, String> getCities(@PathVariable("sid") Integer sid){
		
		return userService.getCities(sid);
		
	} 
	
	// method for uploading the register details
	@PostMapping("/register")
	public String register(RegisterDto regDto, Model model) {
		
		model.addAttribute("countries", userService.getCountries());
		Map<String,String> messages = props.getMessages();
		
		UserDto user = userService.getUser(regDto.getEmail());
		if(user!=null) {
			model.addAttribute(AppConstants.ERROR_MSG, messages.get(AppConstants.DUP_EMAIL));
			
			return "registerView";
		}
		boolean registerUser = userService.registerUser(regDto);
		if(registerUser) {
			model.addAttribute(AppConstants.SUCC_MSG, messages.get(AppConstants.REG_SUCC));
		}else {
			model.addAttribute(AppConstants.ERROR_MSG, messages.get(AppConstants.REG_FAIL));
		}
	
		return "registerView";
	}
	
	// load login page method
	@GetMapping("/")
	public String loginPage(Model model) {
		model.addAttribute("loginDto", new LoginDto());
		return "index";
	}
	
	// login details uploading method
	@PostMapping("/login")
	public String login(LoginDto loginDto, Model model) {
		Map<String,String> messages = props.getMessages();
		UserDto user = userService.getUser(loginDto);
		if(user==null) {
			model.addAttribute(AppConstants.ERROR_MSG, messages.get(AppConstants.INVALID_CREDEANTIALS));
			return "index";
		}
		if("Yes".equals(user.getPwdUpdated())) {
			//pwd already updated- go to dashborad
			return "redirect:dashboard";
		}else {
			//pwd not updated- go to reset pwd page
			ResetPwdDto resetPwdDto=new ResetPwdDto();
			resetPwdDto.setEmail(user.getEmail());
			model.addAttribute("resetPwdDto", resetPwdDto);
			return AppConstants.RESET_PWD_VIEW;
		}

	}
	
	//Reset page loading method
	@PostMapping("/resetPwd")
	public String resetPwd(ResetPwdDto pwdDto, Model model) {
		Map<String,String> messages = props.getMessages();
		if(!(pwdDto.getNewPwd().equals(pwdDto.getConfirmPwd()))) {
			model.addAttribute(AppConstants.ERROR_MSG,messages.get(AppConstants.PWD_MATCH_ERR));
			return AppConstants.RESET_PWD_VIEW;
		}
		
		UserDto user = userService.getUser(pwdDto.getEmail());
		if(user.getPwd().equals(pwdDto.getOldPwd())) {
			boolean resetPwd = userService.resetPwd(pwdDto);
			if(resetPwd) {
				return "redirect:dashboard";
			}else {
				model.addAttribute(AppConstants.ERROR_MSG, messages.get(AppConstants.PWD_UPDATE_ERR));
				return AppConstants.RESET_PWD_VIEW;
			}
			
		}else {
			model.addAttribute(AppConstants.ERROR_MSG, messages.get(AppConstants.OLD_PWD_ERR));
			return AppConstants.RESET_PWD_VIEW;
		}
		
	}
	
	// dashboard method
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		String quote = userService.getQuote();
		model.addAttribute("quote", quote);
		
		return "dashboardView";
	}
	
	// logout page method
	@GetMapping("/logout")
	public String logout() {
		return"redirect:/";
	}

}
