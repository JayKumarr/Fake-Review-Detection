/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yelp.rest.FeatureExtractor;

import com.yelp.database.MySQLConnection;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class PositiveToNegativeRatio_Fast {

    public static void main(String[] args) throws FileNotFoundException, SQLException {
        System.out.println("SQLite Positive to negative ratio");
        Connection connection = MySQLConnection.getInstance().getConnection();
//        String csvFilePathNew = SQliteConnection.getDatabaseDirectory()+"Positive_To_Negative.sql"; // For Membership Length
        String csvFilePathNew = "E:\\yelpResData\\NEW EXPERIMENTS\\TIMESTAMP\\Calc\\Temp\\Positive_To_Negative_2010_11.sql"; // For Membership Length
        PrintStream out = new PrintStream(new FileOutputStream(csvFilePathNew)); // For Membership Length
        Statement createStatement = connection.createStatement();
        HashMap<String, ArrayList<Integer>> reviewerRatings = new HashMap<String, ArrayList<Integer>>();
        ResultSet resultSetReviewers = createStatement.executeQuery("SELECT r.reviewerID from reviewer r ");
        while (resultSetReviewers.next()) {
            String reviewerID = resultSetReviewers.getString("reviewerID");
            reviewerRatings.put(reviewerID, new ArrayList<Integer>());
        }
        resultSetReviewers.close();
        System.out.println("Reviewer IDs collected..!");
        ResultSet resultSetReviewerIDs = createStatement.executeQuery("SELECT r.rating,r.reviewerID from review r ");
        while (resultSetReviewerIDs.next()) {
            String reviewerID = resultSetReviewerIDs.getString("reviewerID");
            ArrayList<Integer> ratingArrayOfReviewer = reviewerRatings.get(reviewerID);
            if(ratingArrayOfReviewer != null){            
                ratingArrayOfReviewer.add(resultSetReviewerIDs.getInt("rating"));
            }

        }
        resultSetReviewerIDs.close();
        createStatement.close();
        System.out.println("Extracted reviews and rating..");
        out.println("INSERT INTO pos_neg_ratio (reviewerID, pn_ratio) VALUES ");
        Iterator<Map.Entry<String, ArrayList<Integer>>> iterator = reviewerRatings.entrySet().iterator();
        int i = 1;
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<Integer>> entry = iterator.next();
            
            String reviewerID = entry.getKey();
            int positiveCount = 1;
            int negativeCount = 1;
            for (int rating : entry.getValue()) {
                if (rating <= 2) {
                    negativeCount++;
                }
                if (rating >= 4) {
                    positiveCount++;
                }
            }
            double postiveToNegativeRatio = positiveCount / negativeCount;
            out.print("('" + reviewerID + "','" + postiveToNegativeRatio + "')");
            if (i % 1000 == 0) {
                out.println(";");
                out.println("INSERT INTO pos_neg_ratio (reviewerID, pn_ratio) VALUES ");
                System.out.println(i);
            } else {
                out.println(",");
            }
            out.flush();
            i++;
        }
    }//main method
}
