package com.parvinder.thapar.bitlinked.rest;

import java.util.List;

public class SimpleSearchResults {
	
	private String queryString;
	private int countSearchResults;
	private List<SimpleCryptoProfile> searchResults;

	public SimpleSearchResults(String queryString, int countSearchResults, List<SimpleCryptoProfile> searchResults) {
		this.queryString = queryString;
		this.countSearchResults = countSearchResults;
		this.searchResults = searchResults;
	}
	
	public String getQueryString() {
		return queryString;
	}
	
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	
	public int getCountSearchResults() {
		return countSearchResults;
	}
	
	public void setCountSearchResults(int countSearchResults) {
		this.countSearchResults = countSearchResults;
	}
	
	public List<SimpleCryptoProfile> getSearchResults() {
		return searchResults;
	}
	
	public void setSearchResults(List<SimpleCryptoProfile> searchResults) {
		this.searchResults = searchResults;
	}

}
