package com.takeawayfood.database;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {
    protected static final String dbClassName = "com.mysql.jdbc.Driver";
    protected static final String CONNECTION = "jdbc:mysql://127.0.0.1/TakeAway";
    protected Connection connection = null;
    protected Statement smt = null;
    protected ResultSet resultSet = null;

    protected void initializeDbConnection() {

        try {
            Class.forName(dbClassName);
            Properties p = new Properties();
            p.put("user", "root");
            p.put("password", "");
            connection = (Connection) DriverManager.getConnection(CONNECTION, p);
            smt = (Statement) connection.createStatement();
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not istalled" + e);
        } catch (SQLException e) {
            System.out.println("Not connected to mysql" + e);
        } catch (Exception e) {
            System.out.println("some other exection");
        }
    }

    protected void closeDbConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Connection can not be ade free: " + e);
            }
        }
    }
}
