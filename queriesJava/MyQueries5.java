package com.oracle.tutorial.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.sql.BatchUpdateException;

public class MyQueries5 {
  
  Connection con;
  JDBCUtilities settings;  
  
  public MyQueries5(Connection connArg, JDBCUtilities settingsArg) {
    this.con = connArg;
    this.settings = settingsArg;
  }

  public static void cursorHoldabilitySupport (Connection conn) throws SQLException {
    
    DatabaseMetaData dbMetaData = conn.getMetaData();

    System.out.println("\nSupports: ");

    System.out.println("ResultSet.HOLD_CURSORS_OVER_COMMIT = " + 
      ResultSet.HOLD_CURSORS_OVER_COMMIT);

    System.out.println("ResultSet.CLOSE_CURSORS_AT_COMMIT = " + 
      ResultSet.CLOSE_CURSORS_AT_COMMIT);

    System.out.println("Default cursor holdability: " + 
      dbMetaData.getResultSetHoldability());

    System.out.println("Supports HOLD_CURSORS_OVER_COMMIT ? " + 
      dbMetaData.supportsResultSetHoldability(
        ResultSet.HOLD_CURSORS_OVER_COMMIT));

    System.out.println("Supports CLOSE_CURSORS_AT_COMMIT ? " + 
      dbMetaData.supportsResultSetHoldability(
        ResultSet.CLOSE_CURSORS_AT_COMMIT));

    System.out.println("Supports TYPE_FORWARD_ONLY with CONCUR_READ_ONLY ? " + 
      dbMetaData.supportsResultSetConcurrency(
        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY));

    System.out.println("Supports TYPE_FORWARD_ONLY with CONCUR_UPDATABLE ? " + 
      dbMetaData.supportsResultSetConcurrency(
        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE));

    System.out.println("Supports TYPE_SCROLL_INSENSITIVE with CONCUR_READ_ONLY ? " + 
      dbMetaData.supportsResultSetConcurrency(
        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY));
    
    System.out.println("Supports TYPE_SCROLL_INSENSITIVE with CONCUR_UPDATABLE ? " + 
      dbMetaData.supportsResultSetConcurrency(
        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE));

    System.out.println("Supports TYPE_SCROLL_SENSITIVE with CONCUR_READ_ONLY ? " + 
      dbMetaData.supportsResultSetConcurrency(
        ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY));

    System.out.println("Supports TYPE_SCROLL_SENSITIVE with CONCUR_UPDATABLE ? " + 
      dbMetaData.supportsResultSetConcurrency(
        ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE));
  }

  public static void modifyPrices(Connection con) throws SQLException {
    Statement stmt = null;
    try {

      stmt = con.createStatement();
      stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
              ResultSet.CONCUR_UPDATABLE);
      
      ResultSet uprs = stmt.executeQuery( "SELECT * FROM COFFEES");

      while (uprs.next()) {
        float f = uprs.getFloat("price");
        uprs.updateFloat("price", f * 1.005F);
        uprs.updateRow();
      }
  
    } catch (SQLException e ) {
      JDBCTutorialUtilities.printSQLException(e);
    } finally {
      if (stmt != null) { stmt.close(); }
    }
  }

  public static void viewPrices (Connection con) throws SQLException {
    Statement stmt = null;
    
    try {

      stmt = con.createStatement();
      stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
              ResultSet.CONCUR_UPDATABLE);
      
      ResultSet uprs = stmt.executeQuery( "SELECT * FROM COFFEES");

      System.out.println("\nPrices cooffes: ");
      while (uprs.next()) {
        float price = uprs.getFloat("price");
        System.out.println(price);
      }
  
    } catch (SQLException e ) {
      JDBCTutorialUtilities.printSQLException(e);
    } finally {
      if (stmt != null) { stmt.close(); }
    }

  }

  public static void viewCoofees (Connection con) throws SQLException {
    Statement stmt = null;
    
    try {

      stmt = con.createStatement();
      stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
              ResultSet.CONCUR_UPDATABLE);
      
      ResultSet uprs = stmt.executeQuery( "SELECT * FROM COFFEES");

      System.out.println("\nCoffees: ");
      while (uprs.next()) {
        Integer sup_id = uprs.getInt("sup_id");
        String cof_name = uprs.getString("cof_name");
        float price = uprs.getFloat("price");
        float sales = uprs.getFloat("sales");
        float total = uprs.getFloat("total");
        System.out.println(sup_id +"\t"+ cof_name +"\t"+ price +"\t"+ sales + "\t" + total);
      }
  
    } catch (SQLException e ) {
      JDBCTutorialUtilities.printSQLException(e);
    } finally {
      if (stmt != null) { stmt.close(); }
    }

  }

  public static void insertCoffees(Connection con) throws SQLException {
    con.setAutoCommit(false);
    try (Statement stmt = con.createStatement()) {

      stmt.addBatch("INSERT INTO coffees (cof_id, cof_name, sup_id, price, sales, total) " +
                    "VALUES(5, 'Amaretto', 150, 9.99, 0, 0)");
      stmt.addBatch("INSERT INTO coffees (cof_id, cof_name, sup_id, price, sales, total) " +
                    "VALUES(6, 'Hazelnut', 150, 9.99, 0, 0)");
      stmt.addBatch("INSERT INTO coffees (cof_id, cof_name, sup_id, price, sales, total) " +
                    "VALUES(7, 'Amaretto_decaf', 150, 10.99, 0, 0)");
      stmt.addBatch("INSERT INTO coffees (cof_id, cof_name, sup_id, price, sales, total) " +
                    "VALUES(8, 'Hazelnut_decaf', 150, 10.99, 0, 0)");

      int[] updateCounts = stmt.executeBatch();
      con.commit();
    } catch (BatchUpdateException b) {
      JDBCTutorialUtilities.printBatchUpdateException(b);
    } catch (SQLException ex) {
      JDBCTutorialUtilities.printSQLException(ex);
    } finally {
      con.setAutoCommit(true);
    }
  }

  public static void main(String[] args) {
    JDBCUtilities myJDBCUtilities;
    Connection myConnection = null;
    if (args[0] == null) {
      System.err.println("Properties file not specified at command line");
      return;
    } else {
      try {
        myJDBCUtilities = new JDBCUtilities(args[0]);
      } catch (Exception e) {
        System.err.println("Problem reading properties file " + args[0]);
        e.printStackTrace();
        return;
      }
    }

    try {
      myConnection = myJDBCUtilities.getConnection();

      MyQueries5.cursorHoldabilitySupport(myConnection);

      System.out.println("\n-- Before update");
      MyQueries5.viewPrices(myConnection);

      MyQueries5.modifyPrices(myConnection);
      
      System.out.println("\n-- After update");
      MyQueries5.viewPrices(myConnection);

      System.out.println("\n-- After insert coffees");
      MyQueries5.insertCoffees(myConnection);
      MyQueries5.viewCoofees(myConnection);

    } catch (SQLException e) {
      JDBCUtilities.printSQLException(e);
    } finally {
      JDBCUtilities.closeConnection(myConnection);
    }

  }
}
