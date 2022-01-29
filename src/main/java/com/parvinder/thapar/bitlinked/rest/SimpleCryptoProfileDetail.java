package com.parvinder.thapar.bitlinked.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleCryptoProfileDetail extends SimpleCryptoProfile {
	
	private List<Map<String, String>> cryptoBlocks;
	
	public SimpleCryptoProfileDetail(String userId, String userFullName, String headline) {
		super(userId, userFullName, headline);
		this.cryptoBlocks = new ArrayList<Map<String, String>>();
	}
	
	public SimpleCryptoProfileDetail(String userId, String userFullName, String headline, String createDate, String lastUpdateDate) {
		super(userId, userFullName, headline, createDate, lastUpdateDate);
		this.cryptoBlocks = new ArrayList<Map<String, String>>();
	}
	

	public List<Map<String, String>> getCryptoBlocks() {
		return this.cryptoBlocks;
	}

	public void setCryptoBlocks(List<Map<String, String>> cryptoBlocks) {
		this.cryptoBlocks = cryptoBlocks;
	}

	public void addCryptoBlock(Map<String,String> cryptoBlockMetadataMap) {
		this.cryptoBlocks.add(cryptoBlockMetadataMap);
	}
	
	@Override
//	@JsonIgnore
	@JsonProperty(value = "searchIndexKeywords")
	public String getSearchIndexKeywords() {
		String searchIndexKeywords = this.userId + " " 
									+ this.userFullName + " " 
									+ this.headline + " " ;
	
		for (Map<String, String> keyValuePairMap: this.cryptoBlocks) {
			for (Map.Entry<String,String> keyValue : keyValuePairMap.entrySet()) {
				if (	"Job Title".equals(keyValue.getKey())
						|| "Company".equals(keyValue.getKey())
						|| "Job Location".equals(keyValue.getKey())
						|| "Job Responsibilities".equals(keyValue.getKey())
						|| "Institution".equals(keyValue.getKey())
						|| "Diploma".equals(keyValue.getKey())
						|| "Majors".equals(keyValue.getKey())
					) {
					searchIndexKeywords += keyValue.getValue().trim().replace("\r", " ").replace("\n", " ").replace("\t", " ") + " ";
				}
			}
		}
		
		return 	searchIndexKeywords;
	}
	
}
