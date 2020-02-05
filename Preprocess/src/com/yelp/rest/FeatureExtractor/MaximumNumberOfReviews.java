/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yelp.rest.FeatureExtractor;

import com.irs2.engine.JTokenizer;
import com.yelp.database.SQliteConnection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class MaximumNumberOfReviews {
    public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {
        File f = new File("D:\\yelpResData\\experiment_2000\\reviewerIDs.csv");
        PrintStream out = new PrintStream(new File(f.getParentFile(),"maximum_number_of_review.csv"));
        BufferedReader reader = new BufferedReader(new FileReader(f));
        String str = null;
        SQliteConnection sqliteconnection = SQliteConnection.getInstance();
        Connection connection = sqliteconnection.getConnection();
        while( (str = reader.readLine())!= null ){
            ResultSet executeQuery = connection.createStatement().executeQuery(" select max(count_review)   from \n" +
                    "  (select count(reviewID) as count_review from review rev where reviewerID='"+str+"' group by rev.date)");
            if(executeQuery.next()){
                int count = executeQuery.getInt(1);
                executeQuery.close();
                if(count==0){
                    ResultSet resultSet = connection.createStatement().executeQuery("SELECT reviewerID from review where reviewID = '"+str+"'");
                    if(resultSet.next()){
                        str = resultSet.getString("reviewerID");
                        resultSet.close();
                        executeQuery = connection.createStatement().executeQuery(" select max(count_review)   from \n" +
                    "  (select count(reviewID) as count_review from review rev where reviewerID='"+str+"' group by rev.date)");
                        if(executeQuery.next()){
                            count = executeQuery.getInt(1);
                            out.println(count);
                        }else{
                            System.out.println("--->>>> problem <<<<-----");
                        }
                        executeQuery.close();
                    }else{
                        System.out.println(str+" may masla he");
                    }
                    
                }else{
                    out.println(count);
                }
            }
        }//end while
        connection.close();
        reader.close();

    }
}
