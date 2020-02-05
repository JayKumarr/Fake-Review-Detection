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
public class AveragePostingRate {
    public static void main(String[] args) throws FileNotFoundException, SQLException {
        System.out.println("MySQL Average Posting Rate");
        Connection connection = MySQLConnection.getInstance().getConnection();
//        String csvFilePathNew = SQliteConnection.getDatabaseDirectory()+"Average_Posting_Rate.sql"; // For Membership Length
        String csvFilePathNew = "E:\\yelpResData\\NEW EXPERIMENTS\\TIMESTAMP\\Average_Posting_Rate_2010_11.sql"; // For Membership Length
        PrintStream out = new PrintStream(new FileOutputStream(csvFilePathNew)); // For Membership Length
        Statement createStatement = connection.createStatement();
        HashMap<String, ArrayList<Integer>> reviewerCountReviewsPerDate = new HashMap<String, ArrayList<Integer>>();
        ResultSet resultSetReviewers = createStatement.executeQuery("SELECT r.reviewerID from reviewer r ");
        while (resultSetReviewers.next()) {
            String reviewerID = resultSetReviewers.getString("reviewerID");
            reviewerCountReviewsPerDate.put(reviewerID, new ArrayList<Integer>());
        }
        resultSetReviewers.close();
        System.out.println("Reviewer IDs collected..!");
        ResultSet resultSetReviewerIDs = createStatement.executeQuery("select reviewerID, count(`reviewID`) as positing_rate from review group by reviewerID, `date` ");
        while (resultSetReviewerIDs.next()) {
            String reviewerID = resultSetReviewerIDs.getString("reviewerID");
            ArrayList<Integer> ratingArrayOfReviewer = reviewerCountReviewsPerDate.get(reviewerID);
            if(ratingArrayOfReviewer != null){            
                ratingArrayOfReviewer.add(resultSetReviewerIDs.getInt("positing_rate"));
            }

        }
        resultSetReviewerIDs.close();
        createStatement.close();
        System.out.println("Extracted reviewer reviews count per day..");
        out.println("INSERT INTO avg_posting_rate (reviewerID, posting_rate) VALUES ");
        Iterator<Map.Entry<String, ArrayList<Integer>>> iterator = reviewerCountReviewsPerDate.entrySet().iterator();
        int i = 1;
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<Integer>> entry = iterator.next();
            
            String reviewerID = entry.getKey();
            int countReviews = 0;
            for (int reviews : entry.getValue()) {
                countReviews+=reviews;
            }
            double averagePostingRate = (double)countReviews / (double)entry.getValue().size();
            
            if(entry.getValue().size()<1){
                continue;
            }else{
                out.print("('" + reviewerID + "','" + averagePostingRate + "')");
            }
            
            if (i % 1000 == 0) {
                out.println(";");
                out.println("INSERT INTO avg_posting_rate (reviewerID, posting_rate) VALUES ");
                System.out.println(i);
            } else {
                out.println(",");
            }
            out.flush();
            i++;
        }//while
    }//main method

}
