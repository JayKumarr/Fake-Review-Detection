/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yelp.database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Administrator
 */
public class MySQLConnection {

    private Connection c = null;
    private static MySQLConnection connection;
    
    public static MySQLConnection getInstance(){
        if(connection == null){
            connection = new MySQLConnection();
        }
        return connection;
    }
    
    public Connection getConnection() {
        if (c == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                c = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_2011_12","root","root");
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
        return c;
    }
}
