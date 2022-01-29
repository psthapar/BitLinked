package com.parvinder.thapar.bitlinked.controller;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.parvinder.thapar.bitlinked.beans.BitLinkedProfile;
import com.parvinder.thapar.bitlinked.blockchain.BlockchainAsset;
import com.parvinder.thapar.bitlinked.blockchain.BlockChainManager;
import com.parvinder.thapar.bitlinked.blockchain.BigChain.BigChainBlockchainAsset;
import com.parvinder.thapar.bitlinked.blockchain.BigChain.BigChainBlockchainManager;

public class ControllerUtil {
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	public static final BlockChainManager blockchainManager = BigChainBlockchainManager.getInstance();
	
	/*
	 * 
	 */
	public static BlockchainAsset createBigChainCryptoProfile(String uniqueUserId) throws Exception {
		return blockchainManager.createBlockChainAsset(uniqueUserId);
	}
	
	/*
	 * 
	 */
	public static void addNewBlock(BitLinkedProfile bitLinkedProfile, Map<String, String> block) throws Exception {
		BlockchainAsset blockchainAsset = new BigChainBlockchainAsset (bitLinkedProfile.getEmail(), bitLinkedProfile.getEncodedKeyPair(), bitLinkedProfile.getProfileAssetId());
		blockchainManager.addNewBlock2Asset(blockchainAsset, block);
	}
	
	/*
	 * 
	 */
	public static List<Map<String, String>> retrieveCryptoBlocks(BitLinkedProfile bitLinkedProfile) throws Exception {
		BlockchainAsset blockchainAsset = new BigChainBlockchainAsset (bitLinkedProfile.getEmail(), bitLinkedProfile.getEncodedKeyPair(), bitLinkedProfile.getProfileAssetId());
		return blockchainManager.retrieveBlocksForAsset(blockchainAsset);
	}
	
	/*
	 * 
	 */
	public static boolean isUserIdValidFormat (String userId) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(userId);
		return matcher.find();
	}


}
