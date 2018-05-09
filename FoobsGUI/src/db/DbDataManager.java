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

import controller.Controller;

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
	

    public String getLatestTweetID (int searchId) throws SQLException {
    	String id = "";
    	con = this.controller.con;
		Statement stmt = null;
	    String query = "SELECT  TOP (1) platformPostID FROM [dbo].[Post]  WHERE [SearchId] = "+searchId+" ORDER BY [CreatedAt] Desc  ";
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
	
	public HashMap getAllTweets () throws SQLException {
		
		HashMap results = new HashMap ();
		
		Statement stmt = null;
	    String query = "select  [platformPostID], [body] From [Sprint-3-InitialCollection].[dbo].[Post] WHERE [body] NOT LIKE '%RT @%' ";
	    Path path = Paths.get(System.getProperty("user.dir")+File.separator+"TestOutput"+File.separator+"output.txt");
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
