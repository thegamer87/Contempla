package org.hr.contempla.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hr.contempla.HRClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import scala.annotation.meta.param;

@RestController
public class ContemplaLoginRestController {
	
	//ex: http://localhost:8080/login?host=https://hr.cineca.it/HRPortal&username=marco.verrocchio@cineca.it&password=
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public boolean login(
			HttpServletRequest request, 
	        HttpServletResponse response,
	        @RequestParam(value="host", required=true) String host, 
	        @RequestParam(value="username", required=true) String username, 
	        @RequestParam(value="password", required=true) String password){
		HRClient hrClient; 
		hrClient = (HRClient)request.getSession().getAttribute("hrclient");
		if (hrClient == null){
			System.out.println("Create new hr client");
			hrClient = new HRClient(host);
		}
		boolean loginResult = false;
		try{
			loginResult = hrClient.login(username, password);		
		} catch (IOException ex){
			loginResult = false;
		}
		
		request.getSession().setAttribute("hrclient", hrClient);
		//if (loginResult){
		//	request.getSession().setAttribute("hrclient", hrClient);
		//}
		return loginResult;	

	}
	
}
