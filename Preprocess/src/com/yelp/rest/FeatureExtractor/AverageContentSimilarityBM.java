/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yelp.rest.FeatureExtractor;

import com.irs2.engine.FrequencyCount;
import com.irs2.engine.Similarity;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class AverageContentSimilarityBM {
    
    public static void main(String[] args) throws SQLException, FileNotFoundException, IOException, ClassNotFoundException {
        File idfFile = new File("E:\\yelpHotelData\\temp\\IDFHAShMap_2010_11.obj");
        File reviewerContentFile = new File(idfFile.getParentFile(),"reviewerContent_2010_11.obj");
        String csvFilePathNew = "AverageContentSimilarityBM.sql"; // For Membership Length
        PrintStream out = new PrintStream(new FileOutputStream(new File(idfFile.getParentFile(), csvFilePathNew))); // For Membership Length
        String insertQuery = "INSERT into content_similarityBM VALUES ";
        
        System.out.println("MySQL Hotel Reviews Content SimilarityBM into database");
        Connection connection = SQliteConnection.getInstance().getConnection();
        HashMap<String, Float> termIDF;
        HashMap<String, ArrayList<String>> reviewerData = new HashMap<>(); // <reviewerID,<reviewContent>>        
        
        long startTime = System.currentTimeMillis();
        if(!idfFile.exists()){
            Statement createStatement = connection.createStatement();
            ResultSet resultSetReviewers = createStatement.executeQuery("SELECT r.reviewerID from reviewer r ");
            while (resultSetReviewers.next()) {
                String reviewerID = resultSetReviewers.getString("reviewerID");
                reviewerData.put(reviewerID, new ArrayList<String>());
            }
            resultSetReviewers.close();
            ResultSet allReviewRS = createStatement.executeQuery("SELECT r.reviewContent,r.reviewerID from review r ");
            LinkedList<String> allReviews = new LinkedList<String>();
            while(allReviewRS.next()){
                String reviewContent = allReviewRS.getString("reviewContent");
                String reviewerID = allReviewRS.getString("reviewerID");
                allReviews.add(reviewContent);
                ArrayList<String> get = reviewerData.get(reviewerID);
                if(get != null){
                    get.add(reviewContent);
                }
                
            }
            allReviewRS.close();
            termIDF = FrequencyCount.calculateIDF(allReviews);
            ObjectOutputStream outIDF = new ObjectOutputStream(new FileOutputStream(idfFile));
            outIDF.writeObject(termIDF);
            outIDF.close();
            
            ObjectOutputStream outReviewerContent = new ObjectOutputStream(new FileOutputStream(reviewerContentFile));
            outReviewerContent.writeObject(termIDF);
            outReviewerContent.close();
            int timeElapsed = (int)((System.currentTimeMillis()-startTime)/1000)/60;
            System.out.println("time consumed for fetching and preparing: minutes "+timeElapsed);
        }else{
            termIDF = (HashMap<String, Float>) (new ObjectInputStream(new FileInputStream(idfFile))).readObject();
            reviewerData = (HashMap<String, ArrayList<String>>) (new ObjectInputStream(new FileInputStream(reviewerContentFile))).readObject();
        }
        
        out.println(insertQuery);
        int count = 0;
        System.out.println("started content similarity..!");
        Iterator<Map.Entry<String, ArrayList<String>>> iterator = reviewerData.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<String>> rsReviewer = iterator.next();            
            count++;
            String reviewerID = rsReviewer.getKey();
            ArrayList<String> userReviews = rsReviewer.getValue();

            double contentSimilarity = contentSimilarity(userReviews, termIDF);
            out.print("('"+reviewerID+"','"+contentSimilarity+"')");
            if(count%1000 ==0){
                out.println(";");
                out.println(insertQuery);
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
                double newCosine = Similarity.cosineSimilarityBM25(reviewToken, innerReview,termIDF,0.5f);
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
