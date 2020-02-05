/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yelp.rest.FeatureExtractor;

import com.yelp.database.SQliteConnection;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class MembershipLength {

    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, SQLException {
//        String csvFilePath = "D:\\yelpResData\\FakeReview.csv";
        String csvFilePathNew = SQliteConnection.getDatabaseDirectory()+"MembershipLength.csv"; // For Membership Length
        PrintStream out = new PrintStream(new FileOutputStream(csvFilePathNew)); // For Membership Length
        Connection connection = SQliteConnection.getInstance().getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT reviewerID, yelpJoinDate from reviewer");
        
//        Reader in = new FileReader(csvFilePath);
//        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
        int count = 0;
        Date currentDate = new Date();
        
//        for (CSVRecord record : records) 
        out.println("INSERT into membership (`reviewerID`, `membership_length`) VALUES ");
        while(resultSet.next()){
            count++;
//            if (count != 1) 
            {
//                String yelpJoinDate = record.get(9);
                String yelpJoinDate = resultSet.getString("yelpJoinDate");
                String reviewerID = resultSet.getString("reviewerID");
//                System.out.print(yelpJoinDate);
                SimpleDateFormat sdf = new SimpleDateFormat("MMMMM yyyy");
                Date parseDate = sdf.parse(yelpJoinDate);
                int difference = getMonthsDifference(parseDate, currentDate); // For Membership Length
                out.print("('"+reviewerID+"','"+difference+"')");   // For Membership Length
                if(count%1000==0){
                    out.println(";");
                    out.println("INSERT into membership (`reviewerID`, `membership_length`) VALUES ");
                }else{
                    out.println(",");
                }
            }
//            else{
//                out.println("membershipLength"); // For Membership Length
//            }
        }
        System.out.println(count);
        resultSet.close();
        connection.close();
    }

    public static final int getMonthsDifference(Date date1, Date date2) {
        int m1 = date1.getYear() * 12 + date1.getMonth();
        int m2 = date2.getYear() * 12 + date2.getMonth();
        return m2 - m1 + 1;
    }
}
