/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yelp.rest.FeatureExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class KFoldGenerator {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File  csvFilePath = new File("E:\\yelpHotelData\\Experiments\\bm-12\\test\\All_Reviews - BM.csv");
        System.out.println(csvFilePath.exists());
        BufferedReader in = new BufferedReader(new FileReader(csvFilePath));
        int k = 10;
        ArrayList<String> lines = new ArrayList<String>();
        String str = null;
        while( (str=in.readLine())!=null ){
            lines.add(str);
        }
        int chunkSize = lines.size()/k;
        int start = 1;
        for(int i=1;i<k;i++){
            int end = start+chunkSize;
            PrintStream out = new PrintStream(new FileOutputStream(new File(csvFilePath.getParentFile(),csvFilePath.getName().replaceAll(".csv", "")+"_"+i+".csv")));
            out.println(lines.get(0));
            for(int j=1;j<lines.size();j++){
                if(j>=start&& j<=end){
                
                }else{
                    out.println(lines.get(j));
                    System.out.println(i+" >> "+j);
                }
            }
            System.out.println("--------------------------");
            for(int temp=start;temp<=end;temp++){
                out.println(lines.get(temp));
                System.out.println(i+" >> "+temp);
            }
            start=end;
        }
        
    }
    
}
