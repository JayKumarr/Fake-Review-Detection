/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yelp.rest.FeatureExtractor;

import com.irs2.engine.StandfordLemmatizer;
import com.yelp.database.SQliteConnection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class CapitalDiversity {
    private static StandfordLemmatizer lemm = new StandfordLemmatizer();
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        Connection connection = SQliteConnection.getInstance().getConnection();
        PrintStream outStream = new PrintStream(new File(SQliteConnection.getDatabaseDirectory(),"CapitalDiversity.sql"));
        ResultSet rsReview = connection.createStatement().executeQuery("SELECT reviewID,reviewContent from review ");
        int count = 0;
        while(rsReview.next()){
            count++;
            String reviewID = rsReview.getString("reviewID");
            try{
                String reviewContent = rsReview.getString("reviewContent");
                DecimalFormat df = new DecimalFormat("#.###");
                double cd = Double.parseDouble(df.format( capitalDiversity(reviewContent)));
                outStream.println("update review SET capital_diversity = '"+cd+"' where reviewID = '"+reviewID+"'");
                if(count%10000 == 0){System.out.println(count);}
                outStream.flush();
            }catch(SQLException | NumberFormatException ex){
                System.err.println("ReviewID : "+reviewID);
                ex.printStackTrace(System.err);
            }
        }
        rsReview.close();
        connection.close();
    }
    
    private static double capitalDiversity(String str){
        List<String> lemmatize = lemm.tokenizer(str);
        int capitalTokens = 0;
        if(lemmatize.isEmpty())return 0;
        for(int i=0;i<lemmatize.size();i++){
            String token = lemmatize.get(i);
            boolean isCapital = Character.isUpperCase( token.charAt(0));
            if(isCapital){
                capitalTokens++;
            }
        }
        return (double)capitalTokens/(double)lemmatize.size();
    }
}
