package com.ajaykarthi.oath2client.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.social.support.FormMapHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class STIClient {

	public static void main(String[] args) {
		STIClient client = new STIClient();
		client.getTokens();
	}

	/*
	 * Add HTTP Authorization header, using Basic-Authentication to send
	 * user-credentials.
	 */
	private HttpHeaders getHeaders() {
		String plainCredentials = "cliente:password";
		String base64Credentials = Base64.getEncoder().encodeToString(plainCredentials.getBytes());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Credentials);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		return headers;
	}
	
	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
		restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
		
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>(2);
		converters.add(new FormMapHttpMessageConverter());
		converters.add(mappingJackson2HttpMessageConverter);
		restTemplate.setMessageConverters(converters);
		
		return restTemplate;
	}
	
	private Map<String, String> getCredentials(){
		Map<String, String> map = new HashMap<>();
		map.put("password", "secret");
		map.put("username", "user");
		map.put("grant_type", "password");
		return map;
	}

	/*
	 * Send a POST request to create a new user.
	 */
	public void getTokens() {
		HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(getCredentials(), getHeaders());
		ResponseEntity<TokenResponse> response = getRestTemplate().exchange("http://localhost:8080/oauth/token", HttpMethod.POST, request, TokenResponse.class);	
		System.out.println(response.getBody());
	}

}
