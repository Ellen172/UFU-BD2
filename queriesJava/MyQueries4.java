package com.oracle.tutorial.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.util.Scanner;
import java.sql.Date;

public class MyQueries4 {
  
  Connection con;
  JDBCUtilities settings;  
  
  public MyQueries4(Connection connArg, JDBCUtilities settingsArg) {
    this.con = connArg;
    this.settings = settingsArg;
  }

  public static void viewDebito (Connection con) throws SQLException {
    Statement stmt = null;
    String query = "SELECT * FROM debito";
   
    try {
      stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      System.out.println("\nDebitos da Instituicao Bancaria: ");
      
      while (rs.next()) {
        Integer numero_debito = rs.getInt("numero_debito");
        float valor_debito = rs.getFloat("valor_debito");
        Integer motivo_debito = rs.getInt("motivo_debito");
        String data_debito = rs.getString("data_debito");
        Integer numero_conta = rs.getInt("numero_conta");
        String nome_agencia = rs.getString("nome_agencia");
        String nome_cliente = rs.getString("nome_cliente");
        
        System.out.println(numero_debito.toString() + "\t" + valor_debito + "\t" + motivo_debito.toString() + "\t" +
                            data_debito + "\t" + numero_conta.toString() + "\t" + nome_agencia + "\t" + nome_cliente);
      }

    } catch (SQLException e) {
      JDBCUtilities.printSQLException(e);

    } finally {
      if (stmt != null) { stmt.close(); }
    }

  }

  public static void getMyData (Connection con) throws SQLException {
    Statement stmt = null;
    String query = "SELECT deposito.nome_cliente, deposito.nome_agencia, deposito.numero_conta, " +
                  "SUM(deposito.saldo_deposito) as soma_deposito, SUM(emprestimo.valor_emprestimo) as soma_emprestimo " +
                  "FROM deposito join emprestimo on deposito.nome_cliente = emprestimo.nome_cliente " + 
                  "GROUP BY deposito.nome_cliente, deposito.nome_agencia, deposito.numero_conta";
   
    try {
      stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      System.out.println("\nContas da Instituicao Bancaria: ");
      
      while (rs.next()) {
        String nomeCliente = rs.getString("nome_cliente");
        String nomeAgencia = rs.getString("nome_agencia");
        Integer nroConta = rs.getInt("numero_conta");
        float somaDeposito = rs.getFloat("soma_deposito");
        float somaEmprestimo = rs.getFloat("soma_emprestimo");
        
        System.out.println(nomeCliente + "\t" + nomeAgencia + "\t" + nroConta.toString() + "\t" +
                            "Deposito: " + somaDeposito + "\tEmprestimo: " + somaEmprestimo);
      }

    } catch (SQLException e) {
      JDBCUtilities.printSQLException(e);

    } finally {
      if (stmt != null) { stmt.close(); }
    }

  }

  public static void modifyDeposito(Connection con, double percentage) throws SQLException {
    Statement stmt = null;
    try {

      stmt = con.createStatement();
      stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
              ResultSet.CONCUR_UPDATABLE);
      
      ResultSet uprs = stmt.executeQuery( "SELECT * FROM deposito");

      while (uprs.next()) {
        float value = uprs.getFloat("saldo_deposito");
        uprs.updateFloat("saldo_deposito", value * (float) percentage);
        uprs.updateRow();
      }
  
    } catch (SQLException e ) {
      JDBCTutorialUtilities.printSQLException(e);
    } finally {
      if (stmt != null) { stmt.close(); }
    }
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

  public static void insertRow(Connection con) throws SQLException {
    Statement stmt = null;

    try {
      stmt = con.createStatement();
      stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
              ResultSet.CONCUR_UPDATABLE);
      
      ResultSet uprs = stmt.executeQuery("SELECT * FROM debito");
      
      uprs.moveToInsertRow(); //posiciona no ponto de inserção da tabela

      uprs.updateInt("numero_debito", 2000);
      uprs.updateFloat("valor_debito", 150F);
      uprs.updateInt("motivo_debito", 1);
      uprs.updateDate("data_debito", Date.valueOf("2014-01-23") );
      uprs.updateInt("numero_conta", 46248);
      uprs.updateString("nome_agencia", "UFU");
      uprs.updateString("nome_cliente", "Carla Soares Sousa");

      uprs.insertRow(); //insere a linha na tabela

      uprs.updateInt("numero_debito", 2001);
      uprs.updateFloat("valor_debito", 200F);
      uprs.updateInt("motivo_debito", 2);
      uprs.updateDate("data_debito", Date.valueOf("2014-01-23") );
      uprs.updateInt("numero_conta", 26892);
      uprs.updateString("nome_agencia", "Glória");
      uprs.updateString("nome_cliente", "Carolina Soares Souza");

      uprs.insertRow(); //insere a linha na tabela

      uprs.updateInt("numero_debito", 2002);
      uprs.updateFloat("valor_debito", 500F);
      uprs.updateInt("motivo_debito", 3);
      uprs.updateDate("data_debito", Date.valueOf("2014-01-23") );
      uprs.updateInt("numero_conta", 70044);
      uprs.updateString("nome_agencia", "Cidade Jardim");
      uprs.updateString("nome_cliente", "Eurides Alves da Silva");

      uprs.insertRow(); //insere a linha na tabela
      uprs.beforeFirst(); //posiciona-se novamente na posição anterior ao primeiro registro

    } catch (SQLException e ) {
      JDBCTutorialUtilities.printSQLException(e);

    } finally {
      if (stmt != null) { stmt.close(); }
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

/*
      MyQueries4.cursorHoldabilitySupport(myConnection);
      
      System.out.println("\n -- Before update");
      MyQueries4.getMyData(myConnection); 
  
      System.out.println("\nDigite o multiplicador como um numero real (Ex.: 5% = 1,05):");
      Scanner in = new Scanner(System.in);
      double percentage = in.nextDouble();

      MyQueries4.modifyDeposito(myConnection, percentage);
      
      System.out.println("\n -- After update (juros 0.5% em deposito)");
      MyQueries4.getMyData(myConnection); 
  
      System.out.println("\n -- After insert ");
*/
      MyQueries4.insertRow(myConnection);
      MyQueries4.viewDebito(myConnection); 

    } catch (SQLException e) {
      JDBCUtilities.printSQLException(e);
    } finally {
      JDBCUtilities.closeConnection(myConnection);
    }

  }
}
