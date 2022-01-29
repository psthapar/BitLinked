package com.parvinder.thapar.bitlinked.exception;

public class BitlinkedException implements BitlinkedExceptionCode {
	private String exceptionCode;
	private String exceptionDetails;
	
	public BitlinkedException(String exceptionCode, String exceptionDetails) {
		this.exceptionCode = exceptionCode;
		this.exceptionDetails = exceptionDetails;
	}
	
	public String getExceptionCode() {
		return exceptionCode;
	}
	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	public String getExceptionDetails() {
		return exceptionDetails;
	}
	public void setExceptionDetails(String exceptionDetails) {
		this.exceptionDetails = exceptionDetails;
	}

}
