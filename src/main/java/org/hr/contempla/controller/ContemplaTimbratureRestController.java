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
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContemplaTimbratureRestController {

	private TimeBean refreshTimeBean(HRClient hrClient, Date date){
		TimeBean tb;
		if (date == null){
			date = new Date();
		}
		try{
			List<RecordTimbratura> timbrature = hrClient.getReport(date);
			tb = ContemplaManager.computeWorkingDay(timbrature, new Date(), date);
		} catch (Exception ex){
			tb = null;
		}
		return tb;
	}
	
	@RequestMapping(value = "/timebean", method = RequestMethod.GET)
	public TimeBean getTimeBean(
			HttpServletRequest request, 
	        HttpServletResponse response,
	        @RequestParam(value="data", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date ){
		
		if (date == null){
			date = new Date();
		}

		HRClient hrClient = (HRClient)request.getSession().getAttribute("hrclient");
		if (hrClient == null){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		
		TimeBean tb = refreshTimeBean(hrClient, date);
		if (tb == null){
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		request.getSession().setAttribute("timebean", tb);
		return tb;
	}
	
	@RequestMapping(value = "/timbrature", method = RequestMethod.GET)
	public List<RecordTimbratura> getTimbrature(
			HttpServletRequest request, 
	        HttpServletResponse response,
	        @RequestParam(value="data", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date ){
			
		TimeBean tb = getTimeBean(request, response, date);
		if (tb == null){
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		else{
			return tb.getTimbratureList();
		}
	}
	
	/*@RequestMapping(value = "/timbrature", method = RequestMethod.POST)
	public TimeBean modifyTimbratura(
			HttpServletRequest request, 
	        HttpServletResponse response,
			@RequestParam(value="action", required=true) String action,
			@RequestParam(value="index", required=true) int index){
		

		
		HRClient hrClient = (HRClient)request.getSession().getAttribute("hrclient");
		if (hrClient == null){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		if (tb == null){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;			
		}
		
		if (action.equals("switchAbilitato")){
			tb.getTimbratureList().get(index).switchEnabled();
		} else if (action.equals("switchVerso")){
			tb.getTimbratureList().get(index).switchDirection();
		} else{
		}
		
		return tb;
	}*/
	
	@RequestMapping(value = "/tempolavorato", method = RequestMethod.GET)
	public Period getTempoLavorato(
			HttpServletRequest request, 
	        HttpServletResponse response,
	        @RequestParam(value="data", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date ){
		TimeBean tb = getTimeBean(request, response, date);
		if (tb == null){
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		else{
			return tb.getWorkedPeriod();
		}
	}
	
	@RequestMapping(value = "/tempomancante", method = RequestMethod.GET)
	public Period getTempoMancante(
			HttpServletRequest request, 
	        HttpServletResponse response,
	        @RequestParam(value="date", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date ){
		TimeBean tb = getTimeBean(request, response, date);
		if (tb == null){
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		else{
			return tb.getRemainingPeriod();
		}
	}
	
	@RequestMapping(value = "/percentualetempo", method = RequestMethod.GET)
	public Integer getPercentualeTempo(
			HttpServletRequest request, 
	        HttpServletResponse response,
	        @RequestParam(value="date", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date ){
		TimeBean tb = getTimeBean(request, response, date);
		if (tb == null){
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		else{
			return tb.getWorkedPercent();
		}
	}
	
	@RequestMapping(value = "/orauscita", method = RequestMethod.GET)
	public DateTime getOraUscita(
			HttpServletRequest request, 
	        HttpServletResponse response,
	        @RequestParam(value="date", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date ){
		TimeBean tb = getTimeBean(request, response, date);
		if (tb == null){
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		else{
			return tb.getExitForecast();
		}
	}
	
	
	
}
