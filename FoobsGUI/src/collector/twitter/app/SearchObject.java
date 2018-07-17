
package collector.twitter.app;
/*
 * Author: Milan Markovic
 * 
 * 
 * 
 */


import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import collector.entity.SearchDetailsEntity;
import collector.entity.SearchLeafNodeEntity;




public class SearchObject extends SearchDetailsEntity{
	

	public String uniqueID;

	public boolean completed =false;
	
	private SearchLeafNodeEntity leafNode;
	
	//default max value so we get the most recent tweet available - i.e. new search

	public SearchLeafNodeEntity getLeafNode() {
		return leafNode;
	}


	public void setLeafNode(SearchLeafNodeEntity leafNode) {
		this.leafNode = leafNode;
	}


	public long lastKonwnID=Long.MAX_VALUE;
	//optional set if some searches were were performed  previously 
		//and this will ensure that once teh ID is found in a search 
		//again the search will not go further in the past
	public long lastKonwnCachedID=0;
	
	public SearchObject () {
		
	}
	

	public void setUniqueID(String string) {
		this.uniqueID = string;
	}
	
	public String getUniqueID() {
		return uniqueID;
	}


	public long getLastKonwnID() {
		return lastKonwnID;
	}


	public void setLastKonwnID(long lastKonwnID) {
		this.lastKonwnID = lastKonwnID;
	}


	public boolean isCompleted() {
		return completed;
	}


	public void setCompleted(boolean completed) {
		this.completed = completed;
	}


	public long getLastKonwnCachedID() {
		return lastKonwnCachedID;
	}


	public void setLastKonwnCachedID(long lastKonwnCachedID) {
		this.lastKonwnCachedID = lastKonwnCachedID;
	}


	
	

}
