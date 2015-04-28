package org.hr.contempla.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hr.contempla.HRClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class ContemplaLoginRestController {
	
	@RequestMapping(method=RequestMethod.POST)
	public boolean login(
			HttpServletRequest request, 
	        HttpServletResponse response,
	        @RequestParam(value="host", required=true) String host, 
	        @RequestParam(value="username", required=true) String username, 
	        @RequestParam(value="password", required=true) String password){
		HRClient hrClient = new HRClient(host);
		boolean loginResult = false;
		try{
			loginResult = hrClient.login(username, password);		
		} catch (IOException ex){
			loginResult = false;
		}
		
		if (loginResult){
			request.getSession().setAttribute("hrClient", hrClient);
		}
		return loginResult;	

	}
	
}
