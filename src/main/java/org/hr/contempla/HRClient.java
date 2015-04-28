package org.hr.contempla;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.hr.contempla.request.LoginRequest;
import org.hr.contempla.request.RecordTimbratura;
import org.hr.contempla.request.ReportRequest;
import org.hr.contempla.request.TimbraturaRequest;

public class HRClient {

    public static String LOGIN_URL             = "/servlet/cp_login";
    public static String TIMBRUS_URL           = "/servlet/ushp_ftimbrus";
    public static String SQL_DATA_PROVIDER_URL = "/servlet/SQLDataProviderServer";
	
	private BasicHttpContext context;
	private String host;	
	
    public HRClient(String host) {
        this.host = host;
        
        context=new BasicHttpContext();
        BasicCookieStore cookieStore=new BasicCookieStore();
        context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    }
	
	public boolean login(String username, String password) throws IOException {
        LoginRequest login = new LoginRequest( new DefaultHttpClient(),context);
        login.setUrl(host+LOGIN_URL);
        login.setUsername(username);
        login.setPassword(password);
        return login.submit().isSuccess();		
	}
	
    public void timbra(String verso) throws IOException {
        TimbraturaRequest timbratura = new TimbraturaRequest(new DefaultHttpClient(),context);
        timbratura.setUrl(host+TIMBRUS_URL);
        if (TimbraturaRequest.VERSO_ENTRATA.equals(verso)) {
           timbratura.entrata();
        } else {
           timbratura.uscita();
        }
    }
    
    public List<RecordTimbratura> getReport(Date date) throws Exception {
        ReportRequest report = new ReportRequest( new DefaultHttpClient(),context);
        report.setUrl(host+SQL_DATA_PROVIDER_URL);
        //return report.getTimbrature(new Date());
        return report.getTimbrature(date);
    }
    
    public List<RecordTimbratura> getReportAndMergeFromInput(Date date, List<RecordTimbratura> otherTimbrature)  throws Exception {
        ReportRequest report = new ReportRequest( new DefaultHttpClient(),context);
        report.setUrl(host+SQL_DATA_PROVIDER_URL);
        List<RecordTimbratura> timbrature = report.getTimbrature(date);
        List<RecordTimbratura> timbratureMerged = new ArrayList<RecordTimbratura>();
        
        if (timbrature.size() < otherTimbrature.size()){
        	timbratureMerged = timbrature;
        }
        else{
            for (int i=0; i<timbrature.size();++i){
            	RecordTimbratura timbratura = timbrature.get(i);
            	RecordTimbratura otherTimbratura;
            	try{
            		otherTimbratura = otherTimbrature.get(i);
            	} catch (ArrayIndexOutOfBoundsException ex){
            		otherTimbratura = timbratura;
            	}
            	
            	            	
            	if (timbratura.equals(otherTimbratura)){
            		timbratureMerged.add(i, timbratura);
            	}
            	else{
            		timbratureMerged.add(i, otherTimbratura);
            	}
            }        	
        }
        return timbratureMerged;
    }
}
