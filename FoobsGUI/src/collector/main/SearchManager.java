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
import collector.twitter.app.AppRESTAPI;
import collector.twitter.app.SearchObject;
import controller.Controller;
import db.DbDataManager;
import db.SearchInfo;
import twitter4j.GeoLocation;

public  class SearchManager {

	public static void continueSearch(ArrayList <SearchInfo> lastSearches, Controller controller) {
		
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
		    	      searchDetails.setKeywords(lastSearches.get(i).getKeywords());
		    	      
		    	      //TO DO fix so it does not create new location, geolocation and geopoint entities
		    	      LocationEntity location = new LocationEntity();
		    	      GeoLocation geoLocation = new   GeoLocation(lastSearches.get(i).getLat(), lastSearches.get(i).getLon());
		    	      GeoPointEntity geoPoint = new GeoPointEntity(geoLocation);
		    	      
		    	      
		    	      geoPoint.setLocationId(location);
		    	      location.setGeoPoint(geoPoint);
		    	      location.setDisplayString(lastSearches.get(i).getSubLabel());	    	        
		    	      searchDetails.setRadius(lastSearches.get(i).getRadius());
		    	    
		    	      System.out.println("Getting last tweet id for searcch "+ lastSearches.get(i).getId());
		    	      String lastTweetId="";
					try {
						lastTweetId = dbDataManager.getLatestTweetID(lastSearches.get(i).getId());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	      System.out.println("Found "+ lastTweetId);
		    	      if (!lastTweetId.equals("")) {
		    	    	  searchDetails.setLastKonwnCachedID(Long.parseLong(lastTweetId));
		    	      }
		    	      
		    	        
		    	      searchDetails.setLocationId(location);
		    	      searchDetails.setNote(lastSearches.get(i).note);
		    	      searchDetails.setStartOfSearch(startDate);
		    	      
					  searches.add(searchDetails); 
		          }
		            PlatformEntity twitter = DAO.getPlatfromBasedOnName("Twitter",controller);
			        AppRESTAPI restAPI = new AppRESTAPI(controller);
			   
			        restAPI.searchKeywordListGeoCodedMultipleSearches(searches);
			        
			        
			        Date endDate = new Date();
		          
			        for (int i =0; i< searches.size(); i++) {
			        	searches.get(i).setEndOfSearch(endDate);
			        	 DAO.saveSearchDetails((SearchDetailsEntity) searches.get(i),controller);
			        }
		          
		          
		          /*
		          
		      //    System.out.println("Keywords loaded from file" + keywords);
		          System.out.println("Preparing search strings for "+note.get(i) );
		          
		    	  ArrayList <String> keywrodssplit = TwitterKeywordSplit.createKeywordAndPhrasesStrings(keywords);
		    	  
		    	  System.out.println("Keyword split results: ");
		    	  
		    	  for (int j =0; j <keywrodssplit.size();j++) {
		    		  System.out.println("Keyword list number: " +(j+1) +" " + keywrodssplit.get(j)+"; length " + keywrodssplit.get(j).length());
		    	  }
		    	 
		    //	  ArrayList <Long> previousIDsFromThisSearch = config.getPreviousTweetIDsForIndividualSearches().get(note.get(i));
		    			  for (int j =0; j <keywrodssplit.size();j++) {
		    		    SearchObject searchDetails = new SearchObject();
		    	        searchDetails.setKeywords(keywrodssplit.get(j));
		    	       
		    	        LocationEntity location = new LocationEntity();
		    	        GeoLocation geoLocation = new   GeoLocation(Double.parseDouble(latitude.get(i)), Double.parseDouble(longitude.get(i)));
		    	        GeoPointEntity geoPoint = new GeoPointEntity(geoLocation);
		    	        geoPoint.setLocationId(location);
		    	        location.setGeoPoint(geoPoint);
		    	        location.setDisplayString(note.get(i));	    	        
		    	        searchDetails.setRadius(Double.parseDouble(radius.get(i)));
		    	        
		    	        if (previousIDsFromThisSearch.size()>j) {
		    	        	searchDetails.setLastKonwnCachedID(previousIDsFromThisSearch.get(j));
		    	        }
		    	        
		    	        searchDetails.setLocationId(location);
		    	        searchDetails.setNote(note.get(i));
		    	        searchDetails.setStartOfSearch(startDate);
		    	        searches.add(searchDetails);
		    	   
		    	        
		    	        
		    	        DAO.saveSearchDetails((SearchDetailsEntity) searchDetails);
		    	  }	  
		      }
	        

	        //ignore the keywords until config is read properly
	      
	        PlatformEntity twitter = DAO.getPlatfromBasedOnName("Twitter");
	        AppRESTAPI restAPI = new AppRESTAPI(config);
	   
	        restAPI.searchKeywordListGeoCodedMultipleSearches(searches);
	        
	        
	        Date endDate = new Date();
	        
	     // populate the following however is suitable
	       
	        //Looop to add the times
	        
	        
	        for (int i =0; i< searches.size(); i++) {
	        	searches.get(i).setEndOfSearch(endDate);
	        	 DAO.saveSearchDetails((SearchDetailsEntity) searches.get(i));
	        }
	      */  
		
		
	}

}
