/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yelp.rest.FeatureExtractor;

import com.irs2.engine.JTokenizer;
import com.yelp.database.SQliteConnection;
import edu.stanford.nlp.io.EncodingPrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ReviewContentLengthSQLite {
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        Connection connection = SQliteConnection.getInstance().getConnection();
        ResultSet executeQuery = connection.createStatement().executeQuery("SELECT reviewID, reviewContent from review");
        PrintStream outUpdateSQL = new PrintStream(new File("E:\\yelpResData\\review_content_length.sql"));
        while(executeQuery.next()){
            String reviewID = executeQuery.getString("reviewID");
            String content = executeQuery.getString("reviewContent");
            List<String> tokens = JTokenizer.getTokens(content, null, false,true);
            outUpdateSQL.println("UPDATE review set content_length="+tokens.size()+" WHERE reviewID='"+reviewID+"';");
        }
        outUpdateSQL.close();
        connection.close();
    }
}
