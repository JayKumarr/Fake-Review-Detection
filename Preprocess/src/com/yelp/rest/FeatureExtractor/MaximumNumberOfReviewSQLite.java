/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yelp.rest.FeatureExtractor;

import com.yelp.database.SQliteConnection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Administrator
 */
public class MaximumNumberOfReviewSQLite {
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        Connection connection = SQliteConnection.getInstance().getConnection();
        ResultSet rsReviewer = connection.createStatement().executeQuery("SELECT reviewerID from reviewer order by reviewerID");
        File csvFilePathNew = new File("E:\\yelpResData\\max_no_of_reviews.sql");
        PrintStream out = new PrintStream(new FileOutputStream(csvFilePathNew)); // For Membership Length
        while(rsReviewer.next()){
            String reviewerID = rsReviewer.getString("reviewerID");
            String query = "select max(count_review) as max_number_of_reviews   from   (select count(reviewID) as count_review from review rev where reviewerID='"+reviewerID+"' group by rev.date)  ;";
            ResultSet rsReviewCount = connection.createStatement().executeQuery(query);
            if(rsReviewCount.next()){
                int maxReviewCount = Integer.parseInt(rsReviewCount.getString("max_number_of_reviews"));
                out.println("UPDATE reviewer SET max_no_of_reviews='"+maxReviewCount+"' WHERE reviewerID = '"+reviewerID+"';");
                
                if(maxReviewCount==0){
                    System.err.println("reviewerID '"+reviewerID+"' has ZERO reviews!");
                }
            }else{
                System.err.println("reviewerID '"+reviewerID+"' not found in review table");
            }
        }  
        
        rsReviewer.close();
        connection.close();
    }
}
