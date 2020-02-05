/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yelp.rest.FeatureExtractor;

import com.irs2.engine.Similarity;
import com.yelp.database.SQliteConnection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class AverageContentSimilarity {
    public static void main(String[] args) throws SQLException, FileNotFoundException, IOException {
//        Connection connection = SQliteConnection.getInstance().getConnection();
        String csvFilePathNew = "E:\\yelpHotelData\\AverageContentSimilarity.sql"; // For Membership Length
        File csvFile = new File(csvFilePathNew);
        csvFile.createNewFile();
        PrintStream out = new PrintStream(new FileOutputStream(csvFilePathNew)); // For Membership Length
        
        System.out.println("SQLite Hotel Reviews Content Similarity into database");
        Connection connection = SQliteConnection.getInstance().getConnection();
        ResultSet rsReviewer = connection.createStatement().executeQuery("SELECT reviewerID from reviewer order by reviewerID");
        out.println("INSERT into content_similarity VALUES ");
        int count = 0;
        while(rsReviewer.next()){
            count++;
            String reviewerID = rsReviewer.getString("reviewerID");
            PreparedStatement prepareStatement = connection.prepareStatement("SELECT reviewContent from review where reviewerID = ?");
            prepareStatement.setString(1, reviewerID);
            ResultSet rsReviewContent = prepareStatement.executeQuery();
            ArrayList<String> userReviews = new ArrayList<String>();
            while(rsReviewContent.next()){
                userReviews.add(rsReviewContent.getString("reviewContent"));
            }
            double contentSimilarity = contentSimilarity(userReviews);
            out.print("('"+reviewerID+"','"+contentSimilarity+"')");
            if(count%1000 ==0){
                out.println(";");
            }else{
                out.println(",");
            }
            out.flush();
        }
        connection.close();
        System.out.println("Process Done");
    }
    
    private static double contentSimilarity(List<String> reviewslist){
        List<List<String>> lemmetizedReviews = new ArrayList<List<String>>();
        for (String rev : reviewslist) {
            lemmetizedReviews.add( Similarity.lemmatize(rev));
        }
        
        double totalSimilarity = 0;
        for (int i=0;i<lemmetizedReviews.size();i++) {
            List<String> reviewToken = lemmetizedReviews.get(i);
            double maxsimilarity = 0d;
            for (int j=0;j<lemmetizedReviews.size();j++) {
                if(j==i)continue;
                List<String> innerReview = lemmetizedReviews.get(j);
                double newCosine = Similarity.cosineSimilarityNNC(reviewToken, innerReview);
                if(newCosine> maxsimilarity){
                    maxsimilarity = newCosine;
                }
            }
            totalSimilarity+=maxsimilarity;
        }
        double averageSimilarity = totalSimilarity/reviewslist.size();
        return averageSimilarity;
    }
}
