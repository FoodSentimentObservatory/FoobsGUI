package db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import Const.GlobalConts;

public class DbConn {

	public static Connection getRootConnection () {
		Connection con = null;
	    try {
	       Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	       con = DriverManager.getConnection("jdbc:sqlserver://"+GlobalConts.SERVER_NAME+"\\MSSQLSERVER;integratedSecurity=true;");
	    } catch (Exception e) {
	       System.err.println("Exception: " + e);
	    } finally {
	      // con.close();
	    }
		return con;
	}
	
	public static void main(String[] args) throws SQLException {
		Connection con = null;
	    try {
	       Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	       con = DriverManager.getConnection("jdbc:sqlserver://"+GlobalConts.SERVER_NAME+"\\MSSQLSERVER;integratedSecurity=true;");

	       DatabaseMetaData meta = con.getMetaData();
	       ResultSet res = meta.getCatalogs();
	       System.out.println("List of databases: "); 
	       while (res.next()) {
	          System.out.println(res.getString("TABLE_CAT"));
	       }
	       res.close();
	    } catch (Exception e) {
	       System.err.println("Exception: " + e);
	    } finally {
	       con.close();
	    }

	}

	public static Connection getDatabaseConnection(String database) {
		Connection con = null;
		System.out.println("Trying to connect to: "+ database);
	    try {
	       Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	       con = DriverManager.getConnection("jdbc:sqlserver://"+GlobalConts.SERVER_NAME+"\\MSSQLSERVER;database="+database+";integratedSecurity=true;");
	    } catch (Exception e) {
	       System.err.println("Exception: " + e);
	    } finally {
	      // con.close();
	    }
		return con;
	}

}
