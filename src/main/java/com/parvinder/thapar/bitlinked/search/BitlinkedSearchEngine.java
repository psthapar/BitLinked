package com.parvinder.thapar.bitlinked.search;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parvinder.thapar.bitlinked.beans.BitLinkedProfile;
import com.parvinder.thapar.bitlinked.blockchain.BlockChainManager;
import com.parvinder.thapar.bitlinked.blockchain.BlockchainAsset;
import com.parvinder.thapar.bitlinked.blockchain.BigChain.BigChainBlockchainAsset;
import com.parvinder.thapar.bitlinked.blockchain.BigChain.BigChainBlockchainManager;
import com.parvinder.thapar.bitlinked.rest.SimpleCryptoProfile;
import com.parvinder.thapar.bitlinked.rest.SimpleCryptoProfileDetail;
import com.parvinder.thapar.bitlinked.rest.SimpleCryptoProfileHeader;
import com.zigurs.karlis.utils.search.QuickSearch;

public class BitlinkedSearchEngine implements SearchEngine {
	private QuickSearch<SimpleCryptoProfile> quickSearchEngine;
	private Map <String, SimpleCryptoProfile> searchDetailIndexMap;
	private Map <String, SimpleCryptoProfile> searchHeaderIndexMap;
	@SuppressWarnings("rawtypes")
	private JpaRepository profileRepository;
	private BlockChainManager blockchainManager = BigChainBlockchainManager.getInstance();

	public BitlinkedSearchEngine() {
		this.quickSearchEngine = new QuickSearch<SimpleCryptoProfile>();
		this.searchDetailIndexMap = new LinkedHashMap <String, SimpleCryptoProfile>();
		this.searchHeaderIndexMap = new LinkedHashMap <String, SimpleCryptoProfile>();
	}
	
	public void setContext(@SuppressWarnings("rawtypes") JpaRepository profileRepository) {
		this.profileRepository = profileRepository;
	}

	/*
	 * 
	 */
	public void fullIndexSearchEngine() throws Exception {
		@SuppressWarnings("unchecked")
		List<BitLinkedProfile> allBitLinkedProfiles = this.profileRepository.findAll();
		System.out.println("\n====>Simple Search Engine: Number of Crypto Profiles : " + allBitLinkedProfiles.size());

		for (BitLinkedProfile cryptoUserProfile: allBitLinkedProfiles) {
			System.out.println("\n====>Simple Search Engine: fullIndexSearchEngine : " + cryptoUserProfile.getEmail() + " : " + cryptoUserProfile.getSummaryprofile());
			this.incrementalIndexSearchEngine (cryptoUserProfile);
		}
		
		System.out.println("\n");
	}
	
	/*
	 * 
	 */
	public void incrementalIndexSearchEngine(BitLinkedProfile bitLinkedProfile) throws Exception {
		// Set the Header Only Profiles with minimum info about profile
		SimpleCryptoProfile simpleCryptoProfileHeader = new SimpleCryptoProfileHeader(bitLinkedProfile.getEmail(), bitLinkedProfile.getName(), bitLinkedProfile.getSummaryprofile(), bitLinkedProfile.getCreateDate(), bitLinkedProfile.getUpdateDate());
		
		// Set the Detail Profiles with all Blocks included
		SimpleCryptoProfile simpleCryptoProfileDetail = new SimpleCryptoProfileDetail(bitLinkedProfile.getEmail(), bitLinkedProfile.getName(), bitLinkedProfile.getSummaryprofile(), bitLinkedProfile.getCreateDate(), bitLinkedProfile.getUpdateDate());
		
		BlockchainAsset blockchainAsset = new BigChainBlockchainAsset (bitLinkedProfile.getEmail(), bitLinkedProfile.getEncodedKeyPair(), bitLinkedProfile.getProfileAssetId());
		List<Map<String, String>> cryptoBlocks = blockchainManager.retrieveBlocksForAsset(blockchainAsset);
		simpleCryptoProfileHeader.setCryptoBlocks(cryptoBlocks);
		simpleCryptoProfileDetail.setCryptoBlocks(cryptoBlocks);

		this.quickSearchEngine.addItem(simpleCryptoProfileDetail, simpleCryptoProfileDetail.getSearchIndexKeywords());
		this.searchHeaderIndexMap.put(simpleCryptoProfileHeader.getUserId(), simpleCryptoProfileHeader);
		this.searchDetailIndexMap.put(simpleCryptoProfileDetail.getUserId(), simpleCryptoProfileDetail);
		
		System.out.println("\n\n");
		System.out.println("==========>Search Index Keywords added for: " + simpleCryptoProfileDetail.getUserId() + ": " + simpleCryptoProfileDetail.getSearchIndexKeywords());
	}

	/*
	 * 
	 */
	public void updateIndexSearchEngine(BitLinkedProfile bitLinkedProfile) throws Exception {
		// Set the Header Only Profiles with minimum info about profile
		SimpleCryptoProfile simpleCryptoProfileHeader = new SimpleCryptoProfileHeader(bitLinkedProfile.getEmail(), bitLinkedProfile.getName(), bitLinkedProfile.getSummaryprofile(), bitLinkedProfile.getCreateDate(), bitLinkedProfile.getUpdateDate());
		
		// Set the Detail Profiles with all Blocks included
		SimpleCryptoProfile simpleCryptoProfileDetail = new SimpleCryptoProfileDetail(bitLinkedProfile.getEmail(), bitLinkedProfile.getName(), bitLinkedProfile.getSummaryprofile(), bitLinkedProfile.getCreateDate(), bitLinkedProfile.getUpdateDate());

		SimpleCryptoProfile existingCryptoProfileInIndex = this.searchDetailIndexMap.get(bitLinkedProfile.getEmail());
		this.quickSearchEngine.removeItem(existingCryptoProfileInIndex);
		this.searchHeaderIndexMap.remove(bitLinkedProfile.getEmail());
		this.searchDetailIndexMap.remove(bitLinkedProfile.getEmail());

		BlockchainAsset bigChainAsset = new BigChainBlockchainAsset (bitLinkedProfile.getEmail(), bitLinkedProfile.getEncodedKeyPair(), bitLinkedProfile.getProfileAssetId());
		List<Map<String, String>> cryptoBlocks = blockchainManager.retrieveBlocksForAsset(bigChainAsset);
		simpleCryptoProfileHeader.setCryptoBlocks(cryptoBlocks);
		simpleCryptoProfileDetail.setCryptoBlocks(cryptoBlocks);

		this.quickSearchEngine.addItem(simpleCryptoProfileDetail, simpleCryptoProfileDetail.getSearchIndexKeywords());
		this.searchHeaderIndexMap.put(simpleCryptoProfileHeader.getUserId(), simpleCryptoProfileHeader);
		this.searchDetailIndexMap.put(simpleCryptoProfileDetail.getUserId(), simpleCryptoProfileDetail);
		
		System.out.println("\n\n");
		System.out.println("==========>Search Index Keywords added for: " + simpleCryptoProfileDetail.getUserId() + ": " + simpleCryptoProfileDetail.getSearchIndexKeywords());

	}

	/*
	 * 
	 */
	public List<SimpleCryptoProfile> findItems(String queryString) throws Exception {
		return this.quickSearchEngine.findItems(queryString, Integer.MAX_VALUE);
	}
	
	/*
	 * 
	 */
	public SimpleCryptoProfile findItemByUserId(String userId) throws Exception {
		return this.searchDetailIndexMap.get(userId);
	}
	
	/*
	 * 
	 */
	public List<SimpleCryptoProfile> findItemHeaders(String queryString) throws Exception {
		return findAllItemHeaders();
	}

	/*
	 * 
	 */
	public List<SimpleCryptoProfile> findAllItemHeaders() throws Exception {
		List<SimpleCryptoProfile> list;
		if (this.searchHeaderIndexMap.values() instanceof List) {
			list = (List<SimpleCryptoProfile>)this.searchHeaderIndexMap.values();	
		} else {
			list = new ArrayList<SimpleCryptoProfile>(this.searchHeaderIndexMap.values());	
		}
		return list;
	}

}
