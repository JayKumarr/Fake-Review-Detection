/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yelp.rest.FeatureExtractor;

import com.yelp.database.MySQLConnection;
import com.yelp.database.SQliteConnection;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class PositiveToNegativeRatio {

    public static void main(String[] args) throws FileNotFoundException, SQLException {
        System.out.println("SQLite Positive to negative ratio");
        Connection connection = MySQLConnection.getInstance().getConnection();
//        String csvFilePathNew = SQliteConnection.getDatabaseDirectory()+"Positive_To_Negative.sql"; // For Membership Length
        String csvFilePathNew = "E:\\yelpResData\\NEW EXPERIMENTS\\TIMESTAMP\\Positive_To_Negative_2010_11.sql"; // For Membership Length
        PrintStream out = new PrintStream(new FileOutputStream(csvFilePathNew)); // For Membership Length
        Statement createStatement = connection.createStatement();
        ResultSet resultSetReviewerIDs = createStatement.executeQuery("SELECT reviewerID from reviewer order by reviewerID");
        ArrayList<String> reviewerIDsList = new ArrayList<>();
        while (resultSetReviewerIDs.next()) {
            reviewerIDsList.add(resultSetReviewerIDs.getString("reviewerID"));
        }
        resultSetReviewerIDs.close();
        createStatement.close();
        out.println("INSERT INTO pos_neg_ratio (reviewerID, pn_ratio) VALUES ");
        for (int i = 0; i < reviewerIDsList.size(); i++) {
            String reviewerID = reviewerIDsList.get(i);
            ResultSet rs = connection.createStatement().executeQuery("SELECT rating from review where reviewerID = '" + reviewerID + "'");
            int positiveCount = 1;
            int negativeCount = 1;
            while (rs.next()) {
                int rating = rs.getInt("rating");
                if (rating <= 2) {
                    negativeCount++;
                }
                if (rating >= 4) {
                    positiveCount++;
                }
            }
            rs.close();
            double postiveToNegativeRatio = positiveCount / negativeCount;
            out.print("('" + reviewerID + "','" + postiveToNegativeRatio + "')");
            if (i % 1000 == 0) {
                out.println(";");
                out.println("INSERT INTO pos_neg_ratio (reviewerID, pn_ratio) VALUES ");
            } else {
                out.println(",");
            }
            out.flush();
        }
    }//main method
}
