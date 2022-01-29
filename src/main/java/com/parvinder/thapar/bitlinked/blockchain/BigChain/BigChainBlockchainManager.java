package com.parvinder.thapar.bitlinked.blockchain.BigChain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bigchaindb.api.AssetsApi;
import com.bigchaindb.api.TransactionsApi;
import com.bigchaindb.builders.BigchainDbConfigBuilder;
import com.bigchaindb.constants.Operations;
import com.bigchaindb.model.Assets;
import com.bigchaindb.model.Transaction;
import com.bigchaindb.model.Transactions;
import com.parvinder.thapar.bitlinked.blockchain.BlockchainAsset;
import com.parvinder.thapar.bitlinked.blockchain.BlockChainManager;

public class BigChainBlockchainManager implements BlockChainManager, BigChainConstants {
	
	private static BigChainBlockchainManager single_instance = null;
	
	public static BigChainBlockchainManager getInstance() {
		if (null == single_instance) {
			single_instance = new BigChainBlockchainManager();
		}
		return single_instance;
	}
	
	private BigChainBlockchainManager() {
		System.out.println("\n\n===> CryptoUtil: setconfig BASE_URL: " + BASE_URL + "\n\n");
		BigchainDbConfigBuilder
		.baseUrl(BASE_URL)
		.addToken("app_id", "")
		.addToken("app_key", "").setup();
	}

	@Override
	public BlockchainAsset createBlockChainAsset(String userId) throws Exception {
		return new BigChainBlockchainAsset (userId);
	}

	@Override
	public void addNewBlock2Asset(BlockchainAsset blockchainAsset, Map<String, String> block) throws Exception {
		blockchainAsset.addNewBlock(block);
		
	}

	@Override
	public List<Map<String, String>> retrieveBlocksForAsset(BlockchainAsset blockchainAsset) throws Exception {
		return this.retrieveCryptoBlocksForProfile(blockchainAsset.getBlockchainAssetId());
	}
	
	/**
	 * 
	 */
	protected List<Map<String, String>> retrieveCryptoBlocksForProfile (String blockchainAssetRootId) throws Exception {
		List<Map<String, String>> crytoBlocks = new ArrayList<Map<String, String>>();
		
		Assets assets = this.searchAssets(blockchainAssetRootId); // should return only 1
		for (int assetId=0; assetId<assets.size(); assetId++) {
			System.out.println("\n ===> Asset ID " + assets.getAssets().get(assetId).getId());

			Transactions createTransactions = TransactionsApi.getTransactionsByAssetId(assets.getAssets().get(assetId).getId(), Operations.CREATE);
			for (int createTransId=0; createTransId<createTransactions.getTransactions().size(); createTransId++) {
				Transaction currTransaction = createTransactions.getTransactions().get(createTransId);
				@SuppressWarnings("unchecked")
				Map<String,String> metadataMap = (Map<String,String>)currTransaction.getMetaData();
				if (null != metadataMap) {
					crytoBlocks.add(metadataMap);	
				}
				
			} // End of for createTransId

			Transactions transferTransactions = TransactionsApi.getTransactionsByAssetId(assets.getAssets().get(assetId).getId(), Operations.TRANSFER);
			for (int transferTransId=0; transferTransId<transferTransactions.getTransactions().size(); transferTransId++) {
				Transaction currTransaction = transferTransactions.getTransactions().get(transferTransId);
				@SuppressWarnings("unchecked")
				Map<String,String> metadataMap = (Map<String,String>)currTransaction.getMetaData();
				if (null != metadataMap) {
					crytoBlocks.add(metadataMap);	
				}
			} // End of for transferTransId
			
		}// End of for Assets
		
		return crytoBlocks;
	}
	
    protected Assets searchAssets(String searchKey) {
    	
    	try {
    		Assets assetsFound =  AssetsApi.getAssets(searchKey);
    		//System.out.println("Number of assets found " + assetsFound.getAssets().size());
    		//System.out.println(assetsFound.getAssets().get(0).getId());
    		return assetsFound;
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    	
    } 
	
	
	
}
