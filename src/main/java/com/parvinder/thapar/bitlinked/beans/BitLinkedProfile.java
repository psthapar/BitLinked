package com.parvinder.thapar.bitlinked.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.parvinder.thapar.bitlinked.util.GlobalUtil;

@Entity
public class BitLinkedProfile implements java.io.Serializable, Comparable<BitLinkedProfile> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "name", length = 256, nullable = true)
    private String name;

    @Column(name = "summaryprofile", length = 600, nullable = true)
    private  String summaryprofile;
    
    @Column(name = "encodedKeyPair", length = 256, nullable = true)
    private  String encodedKeyPair;
    
    @Column(name = "profileAssetId", length = 256, nullable = true)
    private  String profileAssetId;
    
    @Column(name = "createDate", length = 60, nullable = true)
    private  String createDate;
    
    @Column(name = "updateDate", length = 60, nullable = true)
    private  String updateDate;
    
    public BitLinkedProfile() {
    	this.createDate = GlobalUtil.getTodayWithTimeStamp().toString();
    	this.updateDate = GlobalUtil.getTodayWithTimeStamp().toString();
    }
    
    public BitLinkedProfile(String email, String name) {
    	this.email = email;
    	this.name = name;
    	this.createDate = GlobalUtil.getTodayWithTimeStamp().toString();
    	this.updateDate = GlobalUtil.getTodayWithTimeStamp().toString();
    }
    
    public BitLinkedProfile(String email, String name, String summaryprofile) {
    	this.email = email;
    	this.name = name;
    	this.summaryprofile = summaryprofile;
    	this.createDate = GlobalUtil.getTodayWithTimeStamp().toString();
    	this.updateDate = GlobalUtil.getTodayWithTimeStamp().toString();
    }
    
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSummaryprofile() {
		return summaryprofile;
	}

	public void setSummaryprofile(String summaryprofile) {
		this.summaryprofile = summaryprofile;
	}

	public String getEncodedKeyPair() {
		return encodedKeyPair;
	}

	public void setEncodedKeyPair(String encodedKeyPair) {
		this.encodedKeyPair = encodedKeyPair;
	}

	public String getProfileAssetId() {
		return profileAssetId;
	}

	public void setProfileAssetId(String profileAssetId) {
		this.profileAssetId = profileAssetId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	
	@Override
	public int compareTo(BitLinkedProfile otherBitLinkedProfile) {
		// return -1 if different email but this CryptoUserProfile's create date is earlier than the other CryptoUserProfile
		// return +1 if different email but this CryptoUserProfile's create date is later than the other CryptoUserProfile
		// return 0 if same email
		if (this.equals(otherBitLinkedProfile)) {
			return 0;	
		} 
		
//		if ((false == this.equals(otherCryptoProfile)) && 
//					(this.createdate.before(otherCryptoProfile.getCreatedate()))){
//			return -1;
//		}
		return -1;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof BitLinkedProfile) {
			BitLinkedProfile otherCryptoProfile = (BitLinkedProfile)other;
			if (this.email.equals(otherCryptoProfile.getEmail())) {
				return true;
			}
		}
		return false;
	}	

	@Override
	public String toString() {
		return "BitLinkedProfile [email=" + email + ", name=" + name + ", summaryprofile=" + summaryprofile
				+ ", encodedKeyPair=" + encodedKeyPair + ", profileAssetId=" + profileAssetId + ", createDate="
				+ createDate + ", updateDate=" + updateDate + "]";
	}
    
   
}
