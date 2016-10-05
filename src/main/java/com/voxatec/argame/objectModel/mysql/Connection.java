package com.voxatec.argame.objectModel.mysql;

import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Created by retomarti on 10/04/16.
 */

public class Connection {

    private java.sql.Connection conn = null;

    //------ Opening a Connection ---------------------------------------------------------------------
    public void open (String hostName, String dbName, String userName, String password) throws SQLException {
        /* Open a connection to MySQL server */
        String url = String.format("jdbc:mysql://%s/%s", hostName, dbName);
        
        System.out.println("Opening Connection to MySQL database: " + url);

        try {
            Class.forName ("com.mysql.jdbc.Driver").newInstance();
            this.conn = DriverManager.getConnection (url, userName, password);
            System.out.println("Connected");
        }
        catch (Exception e) {
            System.err.println ("Cannot connect to server: " + e.toString());
            this.conn = null;
            System.exit (1);
        }
    }
    
    //------ Check whether Connection is open ------------------------------------------------------------
   public Boolean isOpen() {
    	return this.conn != null;
    }

   //------ Execute SQL Statement with return result -----------------------------------------------------
    public ResultSet executeSelectStatement (String sqlStmt) throws SQLException {
        /* Execute the SQL statement. Returns a result set if any or null. */
        ResultSet resultSet = null;
        Statement stmt = null;

        try {    		
            stmt = this.conn.createStatement();
            boolean hasResultSet = stmt.execute(sqlStmt);
            if (hasResultSet) {
                resultSet = stmt.getResultSet();
            }
            return resultSet;
        }
        catch (Exception e) {
            System.err.println ("Could not execute SQL statement: " + sqlStmt);
            System.err.println ("-> " + e.toString());
            if (stmt != null) {
                stmt.close();
            }
            return null;
        }
    }
    
    //------ Execute SQL Statement without return result --------------------------------------------------
   public void executeUpdateStatement (String sqlStmt) throws SQLException {
    	Statement stmt = null;
    	
    	try {
    		if (this.conn == null) {
    			System.err.println("conn is null");
    		}
    		
    		stmt = this.conn.createStatement();
    		stmt.executeUpdate(sqlStmt);
    	}
    	catch (Exception exception) {
            System.err.println("Could not execute SQL statement: " + sqlStmt);
            System.err.println("-> " + exception.toString());
            exception.printStackTrace();
            if (stmt != null) {
                stmt.close();
            }
    	}
    }

    //------ Closing a Connection ---------------------------------------------------------------------
    public void close () {
        /* Close connection */
        if (this.conn != null) {
            try {
            	this.conn.close();
                System.out.println ("Disconnected");
                this.conn = null;
            }
            catch (Exception e) {
                /* Ignore close errors */
            }
        }
    }

}