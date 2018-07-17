package collector.main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import collector.db.DAO;
import collector.entity.GeoPointEntity;
import collector.entity.LocationEntity;
import collector.entity.PlatformEntity;
import collector.entity.SearchDetailsEntity;
import collector.entity.SearchLeafNodeEntity;
import collector.twitter.app.AppRESTAPI;
import collector.twitter.app.SearchObject;
import controller.Controller;
import db.DbDataManager;
import db.SearchInfo;
import scenes.ContinuedSearchProgress;
import status.SearchInfoBarVBox;
import twitter4j.GeoLocation;

public  class SearchManager {

	public static void continueSearch(ArrayList <SearchInfo> lastSearches, Controller controller,SearchInfoBarVBox  searchInfoBarVBox) {
		
				  DbDataManager dbDataManager = new DbDataManager(controller);
		          Date startDate = new Date();
		          ArrayList<SearchObject> searches = new ArrayList <SearchObject>();
		          System.out.println("We will continue " +lastSearches.size()+ " searches" );
		          for (int i = 0; i<lastSearches.size(); i++ ) {
		        	  System.out.println(lastSearches.get(i).getMainLabel() ); 
		        	  System.out.println(lastSearches.get(i).getSubLabel() ); 
		        	  System.out.println(lastSearches.get(i).getKeywords() );
		        	  System.out.println(lastSearches.get(i).getStarted() );  
		        	  System.out.println(lastSearches.get(i).getLat() );  
		        	  System.out.println(lastSearches.get(i).getLon() );  
		         
		        	  SearchObject searchDetails = new SearchObject();
		    	     
		    	      
		    	     
		    	    
		    	      System.out.println("Getting last tweet id for searcch "+ lastSearches.get(i).getId());
		    	      String lastTweetId="";
					try {
						lastTweetId = dbDataManager.getLatestTweetID(Integer.toString(lastSearches.get(i).getId()));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	      System.out.println("Found "+ lastTweetId);
		    	      if (!lastTweetId.equals("")) {
		    	    	  searchDetails.setLastKonwnCachedID(Long.parseLong(lastTweetId));
		    	      }
		    	      
		    	        
		    	     
		    	      searchDetails.setStartOfSearch(startDate);
		    	      
					  searches.add(searchDetails); 
					  DAO.saveSearchDetails((SearchDetailsEntity) searches.get(i),controller);
		          }
		         
			        AppRESTAPI restAPI = new AppRESTAPI(controller);
			   
			//        restAPI.searchKeywordListGeoCodedMultipleSearches(searches,searchInfoBarVBox);
			        
			        
			        Date endDate = new Date();
		          
			        for (int i =0; i< searches.size(); i++) {
			        	searches.get(i).setEndOfSearch(endDate);
			        	 DAO.saveSearchDetails((SearchDetailsEntity) searches.get(i),controller);
			        }
		          
		          
		        
		
	}

	public static void continueSearch(Controller controller,SearchInfoBarVBox  searchInfoBarVBox) {
		 System.out.println("We will continue " +controller.newSearchObject.getLeafSearches().size()+ " sub searches searches" );
		 HashMap <String,ArrayList <SearchLeafNodeEntity>> map = controller.newSearchObject.getLeafSearches();
		  ArrayList<SearchObject> searches = new ArrayList ();
		 DbDataManager dbDataManager = new DbDataManager(controller);
         Date startDate = new Date();
		 for (String key: map.keySet() ) {
			 System.out.println("For subsearch "+key);
			 for (int i = 0; i<map.get(key).size();i++) {
				 System.out.println( map.get(key).get(i).getLeafSearchLabel());
				 System.out.println( map.get(key).get(i).getKeywords());
				 System.out.println( map.get(key).get(i).getId());
			
				 SearchObject searchDetails = new SearchObject();
				 
				
		         String lastTweetId="";
		         
				 try {
						lastTweetId = dbDataManager.getLatestTweetID(map.get(key).get(i).getLeafSearchLabel());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		 	      System.out.println("Found "+ lastTweetId);
		 	      if (!lastTweetId.equals("")) {
		 	    	  searchDetails.setLastKonwnCachedID(Long.parseLong(lastTweetId));
		 	      }
		 	      searchDetails.setSearchLeafNodeId( map.get(key).get(i).getId());
		 	      searchDetails.setSearchMain(controller.newSearchObject.getMainSearch().getId());
		 	      searchDetails.setSearchSubNode(map.get(key).get(i).getSearchSubNodeId());
		 	      searchDetails.setStartOfSearch(startDate);
		 	      searchDetails.setUniqueID(map.get(key).get(i).getLeafSearchLabel());
		 	      searchDetails.setLeafNode(map.get(key).get(i));
				  searches.add(searchDetails);
				  
				  DAO.saveSearchDetails(searchDetails,controller);
			 
			 }
		 }
		 
		 AppRESTAPI restAPI = new AppRESTAPI(controller);
		   
	        restAPI.searchKeywordListGeoCodedMultipleSearches(searches,controller,searchInfoBarVBox);
	        
	        
	        Date endDate = new Date();
       
	        
	        	for (int i = 0; i<searches.size();i++) {
	        	
	        		searches.get(i).setEndOfSearch(endDate);
	        	DAO.saveSearchDetails(searches.get(i),controller);
	        	}
	        
		 
		 
		 
		 
		
	}

}
