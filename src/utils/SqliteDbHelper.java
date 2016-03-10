/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import models.Setting;

/**
 *
 * @author jaspertomas sqlite-jdbc-3.7.2.jar 
 * downloaded from
 * https://bitbucket.org/xerial/sqlite-jdbc/downloads 
 * tutorial from
 * http://www.tutorialspoint.com/sqlite/sqlite_java.htm
 */
public class SqliteDbHelper {
    public static final String dbfilename="database.db";
    //SINGLETON-----------    
    private static SqliteDbHelper instance=null;
    public static SqliteDbHelper getInstance()
    {
        if(instance==null)
        {
            try {
                instance=new SqliteDbHelper(dbfilename);
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        return instance;
    }    
    private SqliteDbHelper(String filename) throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + filename);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    //MULTI SINGLETON-----------
    /*
    static HashMap<String,SqliteDBHelper>  instances=new HashMap<String,SqliteDBHelper>();
    private  Connection conn = null;
    private  Statement stmt = null;
    private  ResultSet rs = null;


     // Usage:
//      SqliteDBHelper dbm=SqliteDBHelper.getInstance("dbfilename");
//      if(dbm==null)
//      {
//          System.err.println("Error initializing sqlite database instance for "+filename);
//          System.exit(1);
//      }
    public static SqliteDBHelper getInstance(String filename) {
        if(instances.get(filename)==null)
        {
            try{
                instances.put(filename, new SqliteDBHelper(filename));
            }catch(SQLException e){
                return null;
            }
        }
        return instances.get(filename);
    }
    //END SINGLETON--------
    */

    private Connection conn = null;
    private Statement stmt = null;
    
    public Connection getConnection() {
        return getConn();
    }
    public Connection getConn() {
        if(conn==null)
        {
            System.err.println("SQLiteDbHelper not initialized");
            System.exit(1);
        }
        return conn;
    }

    public boolean close()
    {
        try {
            if(!stmt.isClosed())stmt.close();
            if(!conn.isClosed())conn.close();
            return true;
        } catch (SQLException ex) {
            //Logger.getLogger(SQLiteDBHelper.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return false;
        }
    }

//    public static void main(String args[]) {
//        SQLiteDBHelper jdbc = new SQLiteDBHelper();
//        if (jdbc.connect("database.db")) {
//            System.out.println("Opened database successfully");
//        }
//        else
//            System.out.println("Cannot open database");
//
//        if (jdbc.createTable()) {
//            System.out.println("Table created successfully");
//        }
//        
//        if (jdbc.insert()) {
//            System.out.println("Records created successfully");
//        }
//        
//        ArrayList<Item> employees=jdbc.select();
//        if (employees!=null) {
//            for(Item e:employees)
//            {
//                System.out.println( "ID = " + e.id );
//                System.out.println( "NAME = " + e.name );
////                System.out.println( "AGE = " + e.age );
////                System.out.println( "ADDRESS = " + e.address );
////                System.out.println( "SALARY = " + e.salary );
//                System.out.println();            
//            }
//            
//            System.out.println("select successful");
//        }
//        
//        if (jdbc.update()) {
//            System.out.println("update successful");
//        }
//        
//        if (jdbc.delete()) {
//            System.out.println("delete successful");
//        }
//        jdbc.close();
//    }

    public Statement getStatement() throws SQLException
    {
        if(stmt==null)// || stmt.isClosed()
        {
            stmt=conn.createStatement();
        }
        return stmt;
    }
//    public Statement newStatement()
//    {
//        try {
//            stmt=conn.createStatement();
//            return stmt;
//        } catch (SQLException ex) {
//            //Logger.getLogger(SQLiteDBHelper.class.getName()).log(Level.SEVERE, null, ex);
//            ex.printStackTrace();
//            return null;
//        }
//    }
//    public boolean createTable() {
//        try {
//
//            String sql = "CREATE TABLE COMPANY "
//                    + "(ID INT PRIMARY KEY     NOT NULL,"
//                    + " NAME           TEXT    NOT NULL, "
//                    + " AGE            INT     NOT NULL, "
//                    + " ADDRESS        CHAR(50), "
//                    + " SALARY         REAL)";
//            stmt.executeUpdate(sql);
//            stmt.close();
//            
//            return true;
//        } catch (Exception e) {
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            return false;
//        }
//    }
    public static void initialize()
    {
        Setting.createTable();
    }
}
