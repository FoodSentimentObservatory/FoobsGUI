package db;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import collector.entity.GeoRadiusEntity;
import collector.entity.LocationEntity;
import collector.entity.SearchLeafNodeEntity;
import collector.entity.SearchMainEntity;
import collector.entity.SearchSubNodeEntity;
import controller.Controller;
import status.NewSearchObject;

public class DbDataManager {
    
	private Controller controller;
	private Connection con;
	public DbDataManager (Controller controller) {
		this.controller = controller;
		//just for texting get from controller later
		//con = this.controller.con;
	}
	
	public float [] getLocationLatLong (int locationID) throws SQLException {
		
		
		System.out.println(locationID);
		float [] latLong = new float [2];
		
		con = this.controller.con;
		Statement stmt = null;
	    String query = "SELECT   [longitude],[latitude]  FROM [GeoPoint] WHERE [locationId] = "+locationID+" ";
	    try {
		    stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        int count = 0;
	        while (rs.next()) {
	        	latLong[0] = Float.parseFloat(  rs.getString("latitude"));
	        	latLong[1] = Float.parseFloat(  rs.getString("longitude"));
	        }
	        } catch (SQLException e ) {
		    	e.printStackTrace();
	        } finally {
		        if (stmt != null) { stmt.close(); }
		    }
		
		
		return latLong;
		
		
		
		
	}
	
	
	public int getNumberOfTweetsIncludingReTweets () throws SQLException {
		
		int result = -1 ;
		con = this.controller.con;
		Statement stmt = null;
	    String query = "SELECT  COUNT (DISTINCT [platformPostID]) as count FROM [dbo].[Post] ";
	    try {
		    stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        int count = 0;
	        while (rs.next()) {
		      result = Integer.parseInt(  rs.getString("count"));
	        }
	        } catch (SQLException e ) {
		    	e.printStackTrace();
	        } finally {
		        if (stmt != null) { stmt.close(); }
		    }
		
		return result;
	}
	
public int getNumberOfTweetsWithoutReTweets () throws SQLException {
		
		int result = -1 ;
		con = this.controller.con;
		Statement stmt = null;
	    String query = "SELECT  COUNT (DISTINCT [platformPostID]) as count FROM [dbo].[Post]  where [body] NOT LIKE 'RT @%'";
	    try {
		    stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        int count = 0;
	        while (rs.next()) {
		      result = Integer.parseInt(  rs.getString("count"));
	        }
	        } catch (SQLException e ) {
		    	e.printStackTrace();
	        } finally {
		        if (stmt != null) { stmt.close(); }
		    }
		
		return result;
	}
	

    public String getLatestTweetID (String string) throws SQLException {
    	String id = "";
    	con = this.controller.con;
		Statement stmt = null;
	    String query = "SELECT  TOP (1) platformPostID FROM [dbo].[Post]  WHERE [LeafNodeId] = '"+string+"' ORDER BY [CreatedAt] Desc  ";
	    try {
		    stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        int count = 0;
	        while (rs.next()) {
	        	id = rs.getString("platformPostID");
	        }
	        } catch (SQLException e ) {
		    	e.printStackTrace();
	        } finally {
		        if (stmt != null) { stmt.close(); }
		    }
    	
    	return id;
    }

	
	public ArrayList getSearchDetails () throws SQLException {
		con = this.controller.con;
		ArrayList results = new ArrayList ();
		Statement stmt = null;
	    String query = "select  [Id], [StartOfSearch], [EndOfSearch], [LocationId], [Radius], [Note], [Keywords] FROM [dbo].[Search] ";
		//String query = "select  *  ";
	    try {
	    stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        int count = 0;
        while (rs.next()) {
	      SearchInfo obj = new SearchInfo ( Integer.parseInt(  rs.getString("Id")), rs.getString("StartOfSearch"),  rs.getString("EndOfSearch"), Float.parseFloat(  rs.getString("radius")),  Integer.parseInt(  rs.getString("LocationId")), rs.getString("Note"),rs.getString("Keywords"));
          results.add(obj);
        }
        } catch (SQLException e ) {
	    	e.printStackTrace();
        } finally {
	        if (stmt != null) { stmt.close(); }
	    }
	    
		return results;
		
	}
	
	//change the controller.newSearchObject so it holds the info about the selected search
	
	public void updateSearchObject (String mainSearchLabel) throws SQLException {
		
		con = this.controller.con;
		
		Statement stmt = null;
	    String query = "SELECT SearchLeafNode.Id as leafNodeId, SearchLeafNode.LeafSearchLabel as leafSearchLabel, SearchLeafNode.Keywords as keywords,"
	    		+ "[dbo].[SearchSubNode].Id as subId, [dbo].[SearchSubNode].Location as locId, [dbo].[SearchSubNode].SearchNodeLabel as subLabel, [dbo].[SearchSubNode].Description as subDescription,"
	    		+ "[dbo].[SearchMain].Id as mainId, [dbo].[SearchMain].MainSearchLabel as mainLabel, [dbo].[SearchMain].Description as mainDescription,"
	    		+ "[dbo].[Location].locationType as locType, [dbo].[Location].displayString as locDisp,"
	    		+ "[dbo].[GeoRadius].latitude as lat, [dbo].[GeoRadius].longitude as lon, [dbo].[GeoRadius].radius as rad" + 
	    		"  FROM ["+controller.dbName+"].[dbo].[SearchLeafNode]" + 
	    		"  LEft  JOIN [dbo].[SearchSubNode] ON SearchSubNodeId=[dbo].[SearchSubNode].Id" + 
	    		"  LEft  JOIN [dbo].[SearchMain] ON [dbo].[SearchSubNode].SearchMainId=[dbo].[SearchMain].Id" + 
	    		"  LEft  JOIN [dbo].[Location] ON [dbo].[SearchSubNode].Location=[dbo].[Location].Id"+
	    		" LEFT JOIN [dbo].[GeoRadius] ON [dbo].[SearchSubNode].Location = [dbo].[GeoRadius].locationId" + 
	    		"  Where [dbo].[SearchMain].MainSearchLabel ='"+mainSearchLabel+"'";
		//String query = "select  *  ";
	    System.out.println(query);
	    try {
	    stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        int count = 0;
        HashSet subsearchesSeen = new HashSet ();
        HashMap <String, ArrayList <SearchLeafNodeEntity>>leafSearches = new HashMap<String, ArrayList <SearchLeafNodeEntity>>();
        ArrayList <SearchSubNodeEntity> subSearches = new ArrayList <SearchSubNodeEntity> ();
        while (rs.next()) {
        	if (count == 0) {
        		controller.newSearchObject = new NewSearchObject ();
        		SearchMainEntity mainSearch = new SearchMainEntity (rs.getString("mainLabel"), rs.getString("mainDescription"));
        		mainSearch.setId(rs.getInt("mainId"));
        		System.out.println("Adding main search "+rs.getInt("mainId")+":"+rs.getString("mainLabel")+" - "+rs.getString("mainDescription"));
        		controller.newSearchObject.setMainSearch(mainSearch);
        	}
        	
        	String subSearchID = rs.getString("subId");
        	if (!subsearchesSeen.contains(subSearchID)) {
        		LocationEntity loc = new LocationEntity ();
        		loc.setLocationType(rs.getString("locType"));
        		loc.setDisplayString(rs.getString("locDisp"));
        		loc.setId(rs.getInt("locId"));
        		
        		//HANDLE location RADIUS
        		if (rs.getString("locType").equals("GeoRadius")) {
        		GeoRadiusEntity geo = new GeoRadiusEntity ();
        		geo.setLatitude(rs.getDouble("lat"));
        		geo.setLongitude(rs.getDouble("lon"));
        		geo.setLocationId(loc);
        		geo.setRadius(rs.getInt("rad"));
        		loc.setGeoRadius(geo);
        		System.out.println("Adding georadius "+rs.getInt("rad")+"/"+rs.getDouble("lat")+" - "+rs.getDouble("lon"));
        		}
        		
        		
        		SearchSubNodeEntity subSearch = new SearchSubNodeEntity (rs.getString("subLabel"),rs.getString("subDescription"), loc) ;
        		subSearch.setId(rs.getInt("subId"));
        		System.out.println("Adding sub search "+rs.getInt("subId")+":"+rs.getString("subLabel")+" - "+rs.getString("subDescription"));
        		System.out.println("Adding location "+rs.getInt("locId")+":"+rs.getString("locType")+" - "+rs.getString("locDisp"));
        		
        		subsearchesSeen.add(subSearchID);
        		subSearches.add(subSearch);
        		
        		leafSearches.put (rs.getString("subLabel"),new ArrayList<SearchLeafNodeEntity> ());
        	}
        	
        	  SearchLeafNodeEntity leafSearch = new SearchLeafNodeEntity(rs.getString("leafSearchLabel"), rs.getString("keywords"));
        	  leafSearch.setId(rs.getInt("leafNodeId"));
        	  leafSearch.setSearchSubNodeId(rs.getInt("subId"));
        	  
        	  leafSearches.get(rs.getString("subLabel")).add(leafSearch);
        	  
        	  System.out.println("Adding leaf search to  "+rs.getString("subLabel")+":"+rs.getString("leafSearchLabel")+" - "+rs.getString("keywords"));
        	  
        	
        	count++;
        }
        
        
        controller.newSearchObject.setSubSearches(subSearches);
        controller.newSearchObject.setLeafSearches(leafSearches);
        
        } catch (SQLException e ) {
	    	e.printStackTrace();
        } finally {
	        if (stmt != null) { stmt.close(); }
	    }
	    
		
		
		
		
	}
	
	
	public ArrayList <String> getMainSearchLabels () throws SQLException {
		con = this.controller.con;
		ArrayList results = new ArrayList ();
		Statement stmt = null;
	    String query = "select  [MainSearchLabel] FROM [dbo].[SearchMain] ";
		//String query = "select  *  ";
	    try {
	    stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        int count = 0;
        while (rs.next()) {
	        results.add(rs.getString("MainSearchLabel"));
        }
        } catch (SQLException e ) {
	    	e.printStackTrace();
        } finally {
	        if (stmt != null) { stmt.close(); }
	    }
		
		return results;
	}
	
	
	public HashMap getAllTweets () throws SQLException {
		Connection con = DbConn.getDatabaseConnection("FOOBS-SPRINT3-COLLECTION");
		HashMap results = new HashMap ();
		
		Statement stmt = null;
	    String query = "select  [platformPostID], [body] From [FOOBS-SPRINT3-COLLECTION].[dbo].[Post]  Left JOIN [dbo].Search ON [dbo].Search.Id = [dbo].[Post].GeneratedBySearch\r\n" + 
	    		"  where SearchMain =10 ";
	    Path path = Paths.get(System.getProperty("user.dir")+File.separator+"TestOutput"+File.separator+"food_outrageous.txt");
	    try {
	    	BufferedWriter writer = Files.newBufferedWriter(path);
	    	
	    	
	        stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        int count = 0;
	        while (rs.next()) {
	            String body = rs.getString("body");
	            String id = rs.getString("platformPostID");
	            
	           // System.out.println(id + "\t" + body );
	           
	          //  body = body.replaceAll("[^A-Za-z0-9'\\t//]", "");
	            body = body.replaceAll("\n", "");
	            body = body.replaceAll("@", "");
	            body = body.replaceAll("#", "");
	            if (!results.containsValue(body)) {
	             results.put(id, body);
	            }
	            count++;
	        }
	        
	        results.forEach((k,v)-> {
	        try {
	        	String line = v.toString();
				writer.write(line);
				writer.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		    	
            });
	        
	        writer.close();
	        System.out.println(count);
	    } catch (SQLException e ) {
	    	e.printStackTrace();
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        if (stmt != null) { stmt.close(); }
	    }
	    
	    
	    
	    /*
	  //Use try-with-resource to get auto-closeable writer instance
	    try {
	    	BufferedWriter writer = Files.newBufferedWriter(path);
	    	
	    	
	        stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        int count = 0;
	        while (rs.next()) {
	            String body = rs.getString("body");
	            String id = rs.getString("platformPostID");
	            
	           // System.out.println(id + "\t" + body );
	           
	            
	            results.put(id, body);
	            count++;
	        }
	        
	        results.forEach((k,v)-> {
	        try {
				writer.write(v.toString());
				writer.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		    	
            });
	        
	        writer.close();
	        System.out.println(count);
	    } catch (SQLException e ) {
	    	e.printStackTrace();
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        if (stmt != null) { stmt.close(); }
	    }
		*/
		
		return results;
	}
	
public HashSet getDatabaseNames () throws SQLException {
		
		HashSet results = new HashSet ();
		//ok here as we are possibly changing the active connection
		Connection con = DbConn.getRootConnection();
		try {
		       DatabaseMetaData meta = con.getMetaData();
		       ResultSet res = meta.getCatalogs();
		       System.out.println("List of databases: "); 
		       while (res.next()) {
		          System.out.println(res.getString("TABLE_CAT"));
		          results.add(res.getString("TABLE_CAT"));
		       }
		       res.close();
		    } catch (Exception e) {
		       System.err.println("Exception: " + e);
		    } finally {
		       con.close();
		    }

		
		
		return results;
	}
	
	
	
	public static void main(String[] args) {
			
		try {
			System.out.println(new DbDataManager(null).getAllTweets().size());;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
