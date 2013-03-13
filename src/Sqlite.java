/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author chris
 */
public class Sqlite {

    private String sqliteDir = null;
    private String sqliteDb = null;
    private Connection conn = null;
    
    public Sqlite(String dir, String db) {
        this.sqliteDir = dir;
        this.sqliteDb = db;
    }
    
    public boolean openDb() {
        Connection connection = null;

        try {
            Class.forName("org.sqlite.JDBC");

            // create a database connection
            String cn = "jdbc:sqlite:" + this.sqliteDir + "/" + this.sqliteDb;
            this.conn = DriverManager.getConnection(cn);
            return true;
        } catch(SQLException e) {
            // if the error message is "out of memory", 
            // it probably means no database file is found
            System.err.println(e.getMessage());
            Tomboy2Everpad.log.severe(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "+e.getMessage());
            return false;
        } catch(ClassNotFoundException e) {
            System.err.println(Thread.currentThread().getStackTrace()[1].getMethodName() + " ClassNotFound: "+e.getMessage());
            Tomboy2Everpad.log.severe(Thread.currentThread().getStackTrace()[1].getMethodName() + " ClassNotFound: "+e.getMessage());
            return false;
        }
    }
    
    public void sluitDb() {
        try {
            if(this.conn != null) {
              this.conn.close();
            }
        } catch(SQLException e) {
            // if the error message is "out of memory", 
            // it probably means no database file is found
            Tomboy2Everpad.log.severe(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "+e.getMessage());
        }
    }
    
    
    public ResultSet execute(String sql) {
        try {
            Statement statement = this.conn.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery(sql);
            return rs;
        } catch(SQLException e) {
            // if the error message is "out of memory", 
            // it probably means no database file is found
            Tomboy2Everpad.log.severe(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "+e.getMessage());
            return null;
        }
        
    }

    public boolean insert(String sql) {
        try {
            Statement statement = this.conn.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            statement.executeUpdate(sql);
            return true;
        } catch(SQLException e) {
            // if the error message is "out of memory", 
            // it probably means no database file is found
            Tomboy2Everpad.log.severe(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "+e.getMessage());
            return false;
        }
        
    }
}
