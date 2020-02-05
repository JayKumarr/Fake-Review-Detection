package com.yelp.rest.FeatureExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class ShuffleData {
    public static void main(String[] args) throws IOException {
        File csvFile = new File("E:\\yelpHotelData\\All_Reviews.csv");
        split(csvFile, false);
    }
    public static void split(File f, boolean header) throws FileNotFoundException, IOException{
        BufferedReader in = new BufferedReader(new FileReader(f));
        File f1 = new File(f.getAbsolutePath().split(".csv")[0]+"_shuffled.csv");
        PrintStream out1 = new PrintStream(f1);
        String str = null;
        boolean even = false;
        ArrayList<String> list = new ArrayList<>();
        while( (str=in.readLine()) !=null  ){
            list.add(str);
        }
        for(int i=0, j=list.size()-1; i<=j; i++,j--){
            if(i==j){
                out1.println(list.get(i));
            }else{
                out1.println(list.get(i));
                out1.println(list.get(j));
            }
        }
        
        in.close();
        out1.close();
    }
}
