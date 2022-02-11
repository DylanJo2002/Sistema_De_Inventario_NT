package com.nt.Backend_NT.model;

import lombok.Data;

@Data
public class SessionResponse {
	
	private String token;

	public SessionResponse(String token) {
		super();
		this.token = token;
	}
	
	
}
