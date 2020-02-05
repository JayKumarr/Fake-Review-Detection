/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yelp.rest.FeatureExtractor;

import com.yelp.database.SQliteConnection;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author Administrator
 */
public class ReviewDuration {
    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, SQLException {
//        String csvFilePath = "D:\\yelpResData\\FakeReview.csv";
        String csvFilePathNew = SQliteConnection.getDatabaseDirectory()+"ReviewDuration.sql"; // For Membership Length
        PrintStream out = new PrintStream(new FileOutputStream(csvFilePathNew)); // For Membership Length
        Connection connection = SQliteConnection.getInstance().getConnection();
        ResultSet resultSetReviewerIDs = connection.createStatement().executeQuery("SELECT reviewerID from reviewer");
        ArrayList<String> reviewerIDs =new ArrayList<String>();
        while(resultSetReviewerIDs.next()){
            reviewerIDs.add(resultSetReviewerIDs.getString("reviewerID"));
        }
        resultSetReviewerIDs.close();
//        Reader in = new FileReader(csvFilePath);
//        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
        int count = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
        
        
        out.println("INSERT INTO review_duration (reviewerID, rev_duration) VALUES ");
        for (int index = 0;index<reviewerIDs.size();index++) {
            count++;
//            if (count != 1) 
            {
//                String reviewerID = record.get(1);
                String reviewerID = reviewerIDs.get(index);
//                System.out.print(yelpJoinDate);
                String query = "SELECT distinct(r.date) from review as r where r.reviewerID = '"+reviewerID+"'";
//                System.out.println(query);
                ResultSet resultSet = connection.createStatement().executeQuery(query);
                ArrayList<Date> listDates = new ArrayList<>();
                while(resultSet.next()){
                    String postingDate = resultSet.getString(1).trim();
                    try{Date postDate = sdf.parse(postingDate);
                    listDates.add(postDate);
                    }catch(java.text.ParseException ex){
                        System.out.println(postingDate);
                        ex.printStackTrace(System.err);
                        System.exit(-1);
                    }
                }
                Collections.sort(listDates);
                Date first = listDates.get(0);
                Date last = listDates.get(listDates.size()-1);
                long diff = last.getTime() - first.getTime();
                long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                out.print("('"+reviewerID+"','"+days+"')"); // For Membership Length
                if(count%1000 == 0){
                    out.println(";");
                    out.println("INSERT INTO review_duration (reviewerID, rev_duration) VALUES ");
                }else{
                    out.println(",");
                }
            }
//            else{
//                out.println("review_duration"); // For Membership Length
//            }
        }
    }
}
