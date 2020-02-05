/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yelp.rest.FeatureExtractor;

import com.yelp.database.SQliteConnection;
import com.yelp.model.Review;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class ReviewerDeviation {
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        System.out.println("Reviewer Deviation in MySQL database");
        File outputFile = new File(SQliteConnection.getDatabaseDirectory()+"Reviewer_Deviation.sql");
        PrintStream outForReviewerDeviation = new PrintStream(outputFile);
        long startTime = System.currentTimeMillis();
        Connection connection = SQliteConnection.getInstance().getConnection();
        Statement statment = connection.createStatement();
        ResultSet rsRestaurant = statment.executeQuery("SELECT reviewID, rating, hotelID from review order by hotelID");
        System.out.println((System.currentTimeMillis()-startTime)/60000+" minutes for fetching..");
        int resCount = 0;
        int revCount = 0;
        
        outForReviewerDeviation.println("INSERT INTO reviewer_deviation VALUES ");
        HashMap<String,ArrayList<Review>> restaurantsList = new HashMap<>();//<restaurantID, reviews..>
        while(rsRestaurant.next()){
            resCount++;
            String restaurantID= rsRestaurant.getString("hotelID");
            String reviewID= rsRestaurant.getString("reviewID");
            int rating= rsRestaurant.getInt("rating");
            ArrayList<Review> reviews = restaurantsList.get(restaurantID);
            if(reviews == null){
                reviews = new ArrayList<Review>();
                restaurantsList.put(restaurantID, reviews);
            }
            reviews.add(new Review(rating,reviewID));
        }
        rsRestaurant.close();
        
        System.out.println((System.currentTimeMillis()-startTime)/60000+" minutes for storing in list..");
        for (Map.Entry<String, ArrayList<Review>> entrySet : restaurantsList.entrySet()) {
            String restaurantID = entrySet.getKey();
            ArrayList<Review> reviewsList = entrySet.getValue();
            double sumOfAllRating = 0;
            for (Review review : reviewsList) {
                sumOfAllRating+=review.getRating();
            }
            double mean = (double)sumOfAllRating/(double)reviewsList.size();

            for(int i=0;i<reviewsList.size();i++){
                revCount++;
                Review rev = reviewsList.get(i);
                double diff = mean-rev.getRating();
                double absDev = Math.abs(diff);
                outForReviewerDeviation.print("('"+restaurantID+"','"+absDev+"','"+rev.getReviewID()+"')");
                if(revCount%1000==0){
                    outForReviewerDeviation.println(";");
                    outForReviewerDeviation.println("INSERT INTO reviewer_deviation VALUES ");
                    
                }else{
                    outForReviewerDeviation.println(",");
                }

            }
            outForReviewerDeviation.flush();
//            System.out.println((System.currentTimeMillis()-startTime)/60000+" minutes calculation");
            if(resCount%1000==0){
//                System.out.println(resCount);
                System.out.println((System.currentTimeMillis()-startTime)/60000+" minutes for 1000");
                startTime = System.currentTimeMillis();
            }
        }
    }
}
