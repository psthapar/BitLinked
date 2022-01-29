package com.parvinder.thapar.bitlinked.rest;

import com.parvinder.thapar.bitlinked.exception.BitlinkedException;

public class SimpleResponse {
	private String requestName;
	private String response;
	private BitlinkedException exception;
	
	public SimpleResponse (String reqName, String res) {
		this.requestName = reqName;
		this.response = res;
	}
	
	public SimpleResponse (String reqName, String res, BitlinkedException exception) {
		this.requestName = reqName;
		this.response = res;
		this.exception = exception;
	}
	

	public String getRequestName() {
		return requestName;
	}

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public BitlinkedException getException() {
		return exception;
	}

	public void setException(BitlinkedException exception) {
		this.exception = exception;
	}

	
}
