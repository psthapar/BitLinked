package com.parvinder.thapar.bitlinked.blockchain;

import java.util.List;
import java.util.Map;

public interface BlockChainManager {
	
    /**
     * performs CREATE transactions on BigchainDB network
     * @param assetData data to store as asset
     * @param metaData data to store as metadata
     * @param keys keys to sign and verify transaction
     * @return id of CREATED asset
     */
	public BlockchainAsset createBlockChainAsset(String userId) throws Exception;
	   
    /**
     * performs TRANSFER operations on CREATED assets
     * @param txId id of transaction/asset
     * @param metaData data to append for this transaction
     * @param keys keys to sign and verify transactions
     */
	public void addNewBlock2Asset(BlockchainAsset blockchainAsset, Map<String, String> block) throws Exception;
    
    
    /**
     * performs SEARCH and RETREIEVE operations on CREATED assets
     * @param txId id of transaction/asset
     * @param metaData data to append for this transaction
     * @param keys keys to sign and verify transactions
     */
	public List<Map<String, String>> retrieveBlocksForAsset(BlockchainAsset blockchainAsset) throws Exception;
}
