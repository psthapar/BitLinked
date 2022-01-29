package com.parvinder.thapar.bitlinked.rest;

import java.util.List;
import java.util.Map;

public abstract class SimpleCryptoProfile implements Comparable<SimpleCryptoProfile> {
	
	protected String userId;
	protected String userFullName;
	protected String headline;
	protected String createDate;
	protected String lastUpdateDate;
	
	public SimpleCryptoProfile(String userId, String userFullName, String headline) {
		this.userId = userId;
		this.userFullName = userFullName;
		this.headline = headline;
	}
	
	public SimpleCryptoProfile(String userId, String userFullName, String headline, String createDate, String lastUpdateDate) {
		this.userId = userId;
		this.userFullName = userFullName;
		this.headline = headline;
		this.createDate = createDate;
		this.lastUpdateDate = lastUpdateDate;
	}
	
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}
	
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public abstract void setCryptoBlocks(List<Map<String, String>> crytoBlocks);
	
	public abstract String getSearchIndexKeywords();

	@Override
	public int compareTo(SimpleCryptoProfile otherSimpleCryptoProfile) {
		// return -1 if different email but this CryptoUserProfile's create date is earlier than the other CryptoUserProfile
		// return +1 if different email but this CryptoUserProfile's create date is later than the other CryptoUserProfile
		// return 0 if same email
		return this.userId.compareTo(otherSimpleCryptoProfile.getUserId());
	}
}