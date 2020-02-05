/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yelp.rest.FeatureExtractor;

import com.irs2.engine.FrequencyCount;
import com.irs2.engine.Similarity;
import com.yelp.database.MySQLConnection;
import com.yelp.database.SQliteConnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class AverageContentSimilarityLTC {
    
    public static void main(String[] args) throws SQLException, FileNotFoundException, IOException, ClassNotFoundException {
        File idfFile = new File("E:\\yelpResData\\NEW EXPERIMENTS\\TIMESTAMP\\IDFHAShMap_2010_11.obj");
        String csvFilePathNew = "AverageContentSimilarityBM25_2010_11.sql"; // For Membership Length
        PrintStream out = new PrintStream(new FileOutputStream(new File(idfFile.getParentFile(), csvFilePathNew))); // For Membership Length
        
        System.out.println("SQLite Hotel Reviews Content Similarity into database");
        Connection connection = MySQLConnection.getInstance().getConnection();
        HashMap<String, Float> termIDF;
        if(!idfFile.exists()){
            ResultSet allReviewRS = connection.createStatement().executeQuery("SELECT reviewContent from review");
            LinkedList<String> allReviews = new LinkedList<String>();
            while(allReviewRS.next()){
                allReviews.add(allReviewRS.getString("reviewContent"));
            }
            allReviewRS.close();
            termIDF = FrequencyCount.calculateIDF(allReviews);
            ObjectOutputStream outIDF = new ObjectOutputStream(new FileOutputStream(idfFile));
            outIDF.writeObject(termIDF);
            outIDF.close();
        }else{
            termIDF = (HashMap<String, Float>) (new ObjectInputStream(new FileInputStream(idfFile))).readObject();
        }
        
        ResultSet rsReviewer = connection.createStatement().executeQuery("SELECT reviewerID from reviewer order by reviewerID");
        out.println("INSERT into content_similarity VALUES ");
        int count = 0;
        System.out.println("started content similarity..!");
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
            double contentSimilarity = contentSimilarity(userReviews, termIDF);
            out.print("('"+reviewerID+"','"+contentSimilarity+"')");
            if(count%1000 ==0){
                out.println(";");
                out.println("INSERT into content_similarity VALUES ");
            }else{
                out.println(",");
            }
            out.flush();
        }
        connection.close();
        System.out.println("Process Done");
    }
    
    private static double contentSimilarity(List<String> reviewslist,HashMap<String,Float> termIDF ){
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
                double newCosine = Similarity.cosineSimilarityLTC(reviewToken, innerReview, termIDF);
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
