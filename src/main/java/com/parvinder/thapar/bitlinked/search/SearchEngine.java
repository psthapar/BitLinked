package com.parvinder.thapar.bitlinked.search;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parvinder.thapar.bitlinked.beans.BitLinkedProfile;
import com.parvinder.thapar.bitlinked.rest.SimpleCryptoProfile;

public interface SearchEngine {
	public void setContext (JpaRepository profileRepository);
	
	public void fullIndexSearchEngine() throws Exception;
	
	public void incrementalIndexSearchEngine(BitLinkedProfile bitLinkedProfile) throws Exception;

	public void updateIndexSearchEngine(BitLinkedProfile bitLinkedProfile)throws Exception;
	
	public List<SimpleCryptoProfile> findItems(String queryString) throws Exception;
	
	public List<SimpleCryptoProfile> findItemHeaders(String queryString) throws Exception;
	
	public List<SimpleCryptoProfile> findAllItemHeaders() throws Exception;
	
	public SimpleCryptoProfile findItemByUserId(String userId) throws Exception;
	
}
