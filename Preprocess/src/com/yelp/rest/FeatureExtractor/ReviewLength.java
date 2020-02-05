/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yelp.rest.FeatureExtractor;

import com.irs.utils.JTime;
import com.irs2.engine.JTokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ReviewLength {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File f = new File("D:\\yelpResData\\experiment_2000\\review_content.txt");
        PrintStream out = new PrintStream(new File(f.getParentFile(),"review_length.txt"));
        BufferedReader reader = new BufferedReader(new FileReader(f));
        String str = null;
        while( (str = reader.readLine())!= null ){
            List<String> tokens = JTokenizer.getTokens(str, null, false,true);
            out.println(tokens.size());
        }//end while
        reader.close();
    }
}
