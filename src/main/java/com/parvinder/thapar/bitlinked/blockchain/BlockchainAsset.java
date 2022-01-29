package com.parvinder.thapar.bitlinked.blockchain;

import java.util.Map;

public interface BlockchainAsset {
	/*
	 * 
	 */
	public String getUniqueId();

	/*
	 * 
	 */
	public String getBlockchainAssetId();

	/*
	 * 
	 */
	public String getBlockchainEncodedKeys();
	
	/*
	 * 
	 */
	public String addNewBlock (Map <String, String> blockData) throws Exception;

}
