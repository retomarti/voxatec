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

    public void open (String hostName, String dbName, String userName, String password) throws SQLException {
        /* Open a connection to MySQL server */
        String url = String.format("jdbc:mysql://%s/%s", hostName, dbName);
        
        System.out.println("Opening Connection to MySQL database: " + url);

        try {
            Class.forName ("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection (url, userName, password);
            System.out.println("Connected");
        }
        catch (Exception e) {
            System.err.println ("Cannot connect to server: " + e.toString());
            conn = null;
            System.exit (1);
        }
    }

    public ResultSet executeStatement (String sqlStmt) throws SQLException {
        /* Execute the SQL statement. Returns a result set if any or null. */
        ResultSet resultSet = null;
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            boolean hasResultSet = stmt.execute(sqlStmt);
            if (hasResultSet) {
                resultSet = stmt.getResultSet();
            }
//            stmt.close();
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

    public void close () {
        /* Close connection */
        if (conn != null) {
            try {
                conn.close();
                System.out.println ("Disconnected");
                conn = null;
            }
            catch (Exception e) {
                /* Ignore close errors */
            }
        }
    }

}