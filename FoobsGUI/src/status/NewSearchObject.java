package status;

import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.Session;
import org.hibernate.Transaction;

import collector.entity.SearchLeafNodeEntity;
import collector.entity.SearchMainEntity;
import collector.entity.SearchSubNodeEntity;
import controller.Controller;

public class NewSearchObject {
	
	private SearchMainEntity mainSearch ;
	
	private ArrayList <SearchSubNodeEntity> subSearches ;
	
	private HashMap <String,ArrayList <SearchLeafNodeEntity>> leafSearches;
	
	public NewSearchObject () {
		
	}
	
	public void saveSearchDetails (Session session) {
		  if (mainSearch==null||subSearches==null||leafSearches==null) {
			  System.out.println ("Check the new search detail object");
		  }
		  
		  else {
		 
		  
 	      Transaction transaction = session.beginTransaction();
 		  //save main search details
			try {	
				session.saveOrUpdate("test.SearchMainEntity",mainSearch);
			
				int  mainId = mainSearch.getId();
				for (int i =0;i<subSearches.size();i++) {
					
					SearchSubNodeEntity subSearch = subSearches.get(i);
					subSearch.setSearchMainId (mainId);
					session.saveOrUpdate("test.SearchSubNodeEntity",subSearch);
					
					int subId  = subSearch.getId();
					ArrayList <SearchLeafNodeEntity> leafSearchList = leafSearches.get(subSearch.getSearchNodeLabel());
					for (int j = 0; j<leafSearchList.size();j++) {
						SearchLeafNodeEntity leafSearch = leafSearchList.get(j);
						leafSearch.setSearchSubNodeId(subId);
						session.saveOrUpdate("test.SearchLeafNodeEntity",leafSearch);						
						
					}
					
					
				}
				session.getTransaction().commit();
			} catch (Exception e) {
				transaction.rollback();
				e.printStackTrace();
			} finally {
				session.close();
			}
		
		  
			
			
		  }
	}

	public SearchMainEntity getMainSearch() {
		return mainSearch;
	}

	public void setMainSearch(SearchMainEntity mainSearch) {
		this.mainSearch = mainSearch;
	}

	public ArrayList<SearchSubNodeEntity> getSubSearches() {
		return subSearches;
	}

	public void setSubSearches(ArrayList<SearchSubNodeEntity> subSearches) {
		this.subSearches = subSearches;
	}

	public HashMap<String, ArrayList<SearchLeafNodeEntity>> getLeafSearches() {
		return leafSearches;
	}

	public void setLeafSearches(HashMap<String, ArrayList<SearchLeafNodeEntity>> leafSearches) {
		this.leafSearches = leafSearches;
	}

	
	
	
	
}
