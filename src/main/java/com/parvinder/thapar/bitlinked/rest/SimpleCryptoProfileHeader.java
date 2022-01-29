package com.parvinder.thapar.bitlinked.rest;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.parvinder.thapar.bitlinked.util.GlobalConstants;

public class SimpleCryptoProfileHeader extends SimpleCryptoProfile {
	
	private String lastEducation;
	private String lastJob;
	
	public SimpleCryptoProfileHeader(String userId, String userFullName, String headline) {
		super(userId, userFullName, headline);
	}
	
	public SimpleCryptoProfileHeader(String userId, String userFullName, String headline, String createDate, String lastUpdateDate) {
		super(userId, userFullName, headline, createDate, lastUpdateDate);
	}
	
	
	public String getLastEducation() {
		return lastEducation;
	}

	public void setLastEducation(String lastEducation) {
		this.lastEducation = lastEducation;
	}

	public String getLastJob() {
		return lastJob;
	}

	public void setLastJobTitle(String lastJobTitle) {
		this.lastJob = lastJobTitle;
	}
	
//	@JsonIgnore
	@JsonProperty(value = "searchIndexKeywords")
	public String getSearchIndexKeywords() {
		return  this.userId + " " + this.userFullName + " " + this.headline + " " + this.lastEducation + " " + this.lastJob;

	}
	
	public void setCryptoBlocks(List<Map<String, String>> crytoBlocks) {
		for (Map<String, String> currentBlock : crytoBlocks) {
			String blockType = currentBlock.get(GlobalConstants.BLOCK_TYPE);
			
			if(GlobalConstants.BLOCK_TYPE_EDUCATION.equals(blockType)) {
				this.lastEducation = currentBlock.get(GlobalConstants.EDU_DEGREE) + "(" + currentBlock.get(GlobalConstants.EDU_FIELD_OF_STUDY) + ")" + " - " + currentBlock.get(GlobalConstants.EDU_INSTITUTION_NAME);
			} else if (GlobalConstants.BLOCK_TYPE_EMPLOYMENT.equals(blockType)) {
				this.lastJob = currentBlock.get(GlobalConstants.EMP_JOB_TITLE) + " at " + currentBlock.get(GlobalConstants.EMP_COMPANY);
			}
		}
	}
	

}