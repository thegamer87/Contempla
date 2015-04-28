package org.hr.contempla.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.hr.contempla.ContemplaManager;
import org.hr.contempla.HRClient;
import org.hr.contempla.bean.TimeBean;
import org.hr.contempla.request.RecordTimbratura;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/timbrature")
public class ContemplaTimbratureRestController {
	
	@RequestMapping(method=RequestMethod.GET)
	public TimeBean getTimbrature(
			HttpServletRequest request, 
	        HttpServletResponse response,
	        @RequestParam(value="date", required=true) Date date ){
		
		HRClient hrClient = (HRClient)request.getSession().getAttribute("hrClient");
		if (hrClient == null){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		
		TimeBean tb;
		try{
			List<RecordTimbratura> timbrature = hrClient.getReport(date);
			tb = ContemplaManager.computeWorkingDay(timbrature, new Date(), date);
		} catch (Exception ex){
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			tb = null;
		}
		
		return tb;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public TimeBean modifyTimbratura(
			HttpServletRequest request, 
	        HttpServletResponse response,
			@RequestParam(value="timeBean", required=true) TimeBean tb,
			@RequestParam(value="action", required=true) String action,
			@RequestParam(value="index", required=true) int index){
		
		HRClient hrClient = (HRClient)request.getSession().getAttribute("hrClient");
		if (hrClient == null){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		
		if (action.equals("switchAbilitato")){
			tb.getTimbratureList().get(index).switchEnabled();
		} else if (action.equals("switchVerso")){
			tb.getTimbratureList().get(index).switchDirection();
		} else{
		}
		
		return tb;
	}
	
}
