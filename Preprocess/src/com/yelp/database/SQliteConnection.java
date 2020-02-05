package com.yelp.database;

import java.sql.Connection;
import java.sql.DriverManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class SQliteConnection {

    private Connection c = null;
    private static SQliteConnection connection;
    String fileLocation;
    private static final String directory = "E:\\yelpHotelData\\";
    private SQliteConnection() {

    }

    public static SQliteConnection getInstance(){
        if(connection == null){
            connection = new SQliteConnection();
        }
        return connection;
    }
    
    public Connection getConnection() {
        if (c == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:"+directory+"yelpHotelData.db");
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
        return c;
    }

    public static String getDatabaseDirectory() {
        return directory;
    }

    
}
