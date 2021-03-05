package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;


import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FileController {

	  @GetMapping("/file/{id}")
	  public String getFileContent(@PathVariable(value = "id") String id) {
	    return "This is file content:"+id;
	  }
	  
	  //http://localhost:8080/api/url?urlString=http://www.bailii.org/ie/cases/IESC/2017/S22.html - note this is not https
	  @GetMapping("/url")
	  public ResponseEntity<String> getDataFromURL(@RequestParam String urlString ) {
		  
		//--BEGIN------disable SSL certificate verification -----------------
		  
	        // Create a trust manager that does not validate certificate chains
	        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
	                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                    return null;
	                }
	                public void checkClientTrusted(X509Certificate[] certs, String authType) {
	                }
	                public void checkServerTrusted(X509Certificate[] certs, String authType) {
	                }
	            }
	        };
	 
	        // Install the all-trusting trust manager
	        SSLContext sc = null;
			try {
				sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
	        
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	 
	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        };
	 
	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		  
	  //--END------disable certificate verification -----------------
		  
		  
		  
		  HttpHeaders responseHeaders = new HttpHeaders();
		  responseHeaders.set("Access-Control-Allow-Origin","*");
		  		  
		  
		  //String urlString = "http://www.bailii.org/ie/cases/IESC/2017/S22.html";
		  String lines = "";
		  try {
			URL url = new URL(urlString);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
		    while ((line = reader.readLine()) != null)
		    {
		    	lines=lines+line+"\n";
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok()
			      .headers(responseHeaders)
			      .body(lines);
	  }
	
}
