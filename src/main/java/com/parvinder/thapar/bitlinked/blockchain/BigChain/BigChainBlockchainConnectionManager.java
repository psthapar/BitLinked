package com.parvinder.thapar.bitlinked.blockchain.BigChain;

import java.io.IOException;
import java.security.KeyPair;
import java.util.Map;
import java.util.TreeMap;

import com.bigchaindb.api.AssetsApi;
import com.bigchaindb.api.TransactionsApi;
import com.bigchaindb.builders.BigchainDbTransactionBuilder;
import com.bigchaindb.constants.Operations;
import com.bigchaindb.model.Assets;
import com.bigchaindb.model.FulFill;
import com.bigchaindb.model.GenericCallback;
import com.bigchaindb.model.MetaData;
import com.bigchaindb.model.Transaction;
import com.bigchaindb.model.Transactions;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import okhttp3.Response;

public class BigChainBlockchainConnectionManager {
    /**
     * performs CREATE transactions on BigchainDB network
     * @param assetData data to store as asset
     * @param metaData data to store as metadata
     * @param keys keys to sign and verify transaction
     * @return id of CREATED asset
     */
    public String doCreate(Map<String, String> assetData, KeyPair keys) throws Exception {

        try {
        //build and send CREATE transaction
        Transaction transaction = null;
        
             transaction = BigchainDbTransactionBuilder
                    .init()
                    .addAssets(assetData, TreeMap.class)
                    .operation(Operations.CREATE)
                    .buildAndSign((EdDSAPublicKey) keys.getPublic(), (EdDSAPrivateKey) keys.getPrivate())
                    .sendTransaction(handleServerResponse());

            System.out.println("(*) CREATE Transaction sent.. - " + transaction.getId());
            
            //let the transaction commit in block
            Thread.sleep(1000);
            return transaction.getId();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }	

    /**
     * performs CREATE transactions on BigchainDB network
     * @param assetData data to store as asset
     * @param metaData data to store as metadata
     * @param keys keys to sign and verify transaction
     * @return id of CREATED asset
     */
    public String doCreate(Map<String, String> assetData, MetaData metaData, KeyPair keys) throws Exception {

        try {
        //build and send CREATE transaction
        Transaction asset = null;
        
        asset = BigchainDbTransactionBuilder
                    .init()
                    .addAssets(assetData, TreeMap.class)
                    .addMetaData(metaData)
                    .operation(Operations.CREATE)
                    .buildAndSign((EdDSAPublicKey) keys.getPublic(), (EdDSAPrivateKey) keys.getPrivate())
                    .sendTransaction(handleServerResponse());

            System.out.println("(*) CREATE Transaction sent.. - " + asset.getId());
            
            //let the transaction commit in block
            Thread.sleep(1000);
            return asset.getId();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
    
    /**
     * performs TRANSFER operations on CREATED assets
     * @param txId id of transaction/asset
     * @param metaData data to append for this transaction
     * @param keys keys to sign and verify transactions
     */
    public String doAppend(String assetId, MetaData metaData, KeyPair keys) throws Exception {
    	return this.doAppend(assetId, this.getLastTransactionId(assetId), metaData, keys);
    }    
    
    
    /**
     * performs TRANSFER operations on CREATED assets
     * @param txId id of transaction/asset
     * @param metaData data to append for this transaction
     * @param keys keys to sign and verify transactions
     */
    protected String doAppend(String assetId, String lastTxnId, MetaData metaData, KeyPair keys) throws Exception {
    	System.out.println("\n===> Do Transfer: Create TXID: " + assetId);
    	System.out.println("===> Do Transfer: Last TXID: " + lastTxnId + "\n");
    	
        try {
        	
        	if (null == lastTxnId || 0 == lastTxnId.length()) {
        		System.out.println("===> Last TXID is NULL or ZERO LENGTH \n");
        		lastTxnId = assetId;
        	}

            //which transaction you want to fulfill?
            FulFill fulfill = new FulFill();
            fulfill.setOutputIndex(0);
            fulfill.setTransactionId(lastTxnId);
            
            //build and send TRANSFER transaction
            Transaction transaction = BigchainDbTransactionBuilder
                    .init()
                    .addInput(null, fulfill, (EdDSAPublicKey) keys.getPublic())
                    .addOutput("1", (EdDSAPublicKey) keys.getPublic())
                    .addAssets(assetId, String.class)
                    .addMetaData(metaData)
                    .operation(Operations.TRANSFER)
                    .buildAndSign((EdDSAPublicKey) keys.getPublic(), (EdDSAPrivateKey) keys.getPrivate())
                    .sendTransaction(handleServerResponse());

            System.out.println("(*) TRANSFER Transaction sent.. - " + transaction.getId());

            //let the transaction commit in block
            Thread.sleep(1000);
            return transaction.getId();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }    
    
    private void onSuccess(Response response) {
        System.out.println("Transaction posted successfully: \n" + response.body() + "\n" + response.message() + "\n" + response);
        //TODO : Add your logic here with response from server        
        
        // Initiate Database Storage of lastTransaction Id, lastUpdateDate, lastUpdateby
    }
    
    private void onFailure() {
        //TODO : Add your logic here
        System.out.println("Transaction failed");
    }
    
    private GenericCallback handleServerResponse() {
        //define callback methods to verify response from BigchainDBServer
        GenericCallback callback = new GenericCallback() {

            @Override
            public void transactionMalformed(Response response) {
            	System.out.println(response);
                System.out.println("malformed " + response.message());
                onFailure();
            }

            @Override
            public void pushedSuccessfully(Response response) {
            	System.out.println(response);
                System.out.println("pushedSuccessfully");
                onSuccess(response);
        }

            @Override
            public void otherError(Response response) {
                System.out.println("otherError" + response.message());
                onFailure();
            }
        };
        
        return callback;
    }
    
	protected String getLastTransactionId(final String assetId) throws Exception{
    	String lastTransactionId = null;
  		Assets assetsFound =  AssetsApi.getAssets(assetId);
  		
  		if (null == assetsFound || null == assetsFound.getAssets() || assetsFound.getAssets().size() == 0) {
  			throw new Exception ("ASSET NOT FOUND");
  		}
  		
  		if (assetsFound.getAssets().size() > 1) {
  			throw new Exception ("More than one Unique asset found. Please refine your search");
  		}
  	
 	
  		Transactions transferTransactions = TransactionsApi.getTransactionsByAssetId(assetId, Operations.TRANSFER);
  		if (null == transferTransactions.getTransactions() || transferTransactions.getTransactions().size() == 0) {
  			transferTransactions = TransactionsApi.getTransactionsByAssetId(assetId, Operations.CREATE);
  		}
  		
  		if (null == transferTransactions || transferTransactions.getTransactions().size() == 0) {
  			return assetId;
  		} 
  		
  		lastTransactionId = transferTransactions
  							.getTransactions()
  							.get(transferTransactions.getTransactions().size()-1)
  							.getId().replaceAll("\"","");
  		
  		System.out.println("\n\n===> getLastTransaction: Returning LAST TXID: " + lastTransactionId.replaceAll("\"","") + "\n");
  		
  		return lastTransactionId;
    }
}
