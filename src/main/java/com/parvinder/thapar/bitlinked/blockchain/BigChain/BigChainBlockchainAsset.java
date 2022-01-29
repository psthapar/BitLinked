package com.parvinder.thapar.bitlinked.blockchain.BigChain;

import java.io.Serializable;
import java.security.KeyPair;
import java.util.Map;
import java.util.TreeMap;

import com.bigchaindb.model.MetaData;
import com.parvinder.thapar.bitlinked.blockchain.BlockchainAsset;
import com.parvinder.thapar.bitlinked.blockchain.util.BlockchainUtil;

public class BigChainBlockchainAsset implements BlockchainAsset, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String uniqueId;
	private String blockchainAssetId;
	private String blockchainEncodedKeys;
	private KeyPair keys;

	/**
	 * 
	 */
	public BigChainBlockchainAsset (String uniqueId) throws Exception {
		this.uniqueId = uniqueId;
		this.keys = BlockchainUtil.createNewCryptoKeys();
		this.blockchainAssetId = this.createCryptoAsset(this.uniqueId, this.keys);
		this.blockchainEncodedKeys = BlockchainUtil.enCodeKeyPair(this.keys);
	}
	
	/**
	 * 
	 */
	public BigChainBlockchainAsset (String uniqueId, String encodedKeyPair, String assetId) throws Exception {
		this.uniqueId = uniqueId;
		this.blockchainAssetId = assetId;
		this.blockchainEncodedKeys = encodedKeyPair;
		this.keys = BlockchainUtil.resurrectKeyPairFromString(encodedKeyPair);
	}
	
	/**
	 * 
	 */
	public BigChainBlockchainAsset (String uniqueId, String encodedKeyPair, String assetId, KeyPair keys) throws Exception {
		this.uniqueId = uniqueId;
		this.blockchainAssetId = assetId;
		this.blockchainEncodedKeys = encodedKeyPair;
		this.keys = keys;
	}
	
	@Override
	public String addNewBlock(Map<String, String> blockData) throws Exception {
		// 1. Peform any auditing work
		// 2. Commit to Blockchain
    	return this.appendCryptoProfile(blockData);
	}
	

	@Override
	public String getUniqueId() {
		return this.uniqueId;
	}

	@Override
	public String getBlockchainAssetId() {
		return this.blockchainAssetId;
	}

	@Override
	public String getBlockchainEncodedKeys() {
		return this.blockchainEncodedKeys;
	}
	
	public KeyPair getKeys() {
		return keys;
	}

	public void setKeys(KeyPair keys) {
		this.keys = keys;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public void setBlockchainAssetId(String blockchainAssetId) {
		this.blockchainAssetId = blockchainAssetId;
	}

	public void setBlockchainEncodedKeys(String blockchainEncodedKeys) {
		this.blockchainEncodedKeys = blockchainEncodedKeys;
	}
	
	/**
	 * 
	 */
	protected String createCryptoAsset(String uniqueId, KeyPair keys) throws Exception{
		BigChainBlockchainConnectionManager cryptoConnManager = new BigChainBlockchainConnectionManager();
		
    	@SuppressWarnings("serial")
		Map<String, String> assetData = new TreeMap<String, String>() {{
            put("UniqueId", uniqueId);
        }};
        
        return cryptoConnManager.doCreate(assetData, keys);
	}

	/**
	 * 
	 */
	protected String appendCryptoProfile(Map <String, String> userData) throws Exception{
		BigChainBlockchainConnectionManager cryptoConnManager = new BigChainBlockchainConnectionManager();
		
		MetaData metadata = new MetaData();
		//metadata.setMetaData(userData);
		if (userData != null) {
    		for (Map.Entry<String,String> entry : userData.entrySet()) {  
    			metadata.setMetaData(entry.getKey(), entry.getValue());
    		} 
		}		
		
		return cryptoConnManager.doAppend(this.blockchainAssetId, metadata, this.keys);
	}

	@Override
	public String toString() {
		return "BigChainBlockchainAsset [uniqueId=" + uniqueId + ", blockchainAssetId=" + blockchainAssetId
				+ ", blockchainEncodedKeys=" + blockchainEncodedKeys + ", keys=" + keys + "]";
	}

}
