/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yelp.rest.FeatureExtractor;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Iterator;

/**
 *
 * @author Administrator
 */
public class NormalizeDataWithOpenCSV {

    public static void main(String[] args)  {
        boolean ignoreFirstRow = true;
        boolean ignoreLastColumn = true;
        File csvFilePath = new File("E:\\yelpHotelData\\All_Reviews_shuffled-unnormalized.csv");
        String outFilenameString = csvFilePath.getName().split(".csv")[0]+"_[normalized].csv";
        DecimalFormat decimalformatter = new DecimalFormat("#.####");
        int count = 0;
        try{
            CSVReader csvReader = new CSVReader(new FileReader(csvFilePath));
            int colSize = 0;
            String[] row = csvReader.readNext();
            if(ignoreFirstRow)row = csvReader.readNext();
            colSize = row.length;
            if(ignoreLastColumn)colSize--;
            System.out.println("columns: "+colSize);
            Float[] minimum = new Float[colSize]; // minimum of all columns
            Float[] maximum = new Float[colSize];// maximum of all columns
            //setting initial values of maximum and minimum of first row
            for (int i = 0; i < colSize; i++) {
                try{
                    minimum[i] = Float.parseFloat(row[i]);
                    maximum[i] = Float.parseFloat(row[i]);
                }catch(java.lang.NumberFormatException ex){
                    System.out.println("column:"+i);
                    ex.printStackTrace(System.err);
                    System.exit(-1);
                }
            }


            while ((row = csvReader.readNext()) != null) {
                Float[] rowArray = new Float[row.length];
                for (int j = 0; j < colSize; j++) {
                    rowArray[j] = Float.parseFloat(row[j]);
                }
                for (int i = 0; i < colSize; i++) {
                    if(minimum[i]>rowArray[i])minimum[i] = rowArray[i];
                    if(maximum[i]<rowArray[i])maximum[i] = rowArray[i];
                }
                if(count%100==0)System.out.println(count);
                count++;   
            }

            //we have stored minumum and maximum of every column
            System.out.println("Minimum and maximum are extracted..");
            // Now we have to traverse again and normalize every row
            csvReader = new CSVReader(new FileReader(csvFilePath));
            Iterator<String[]> csvIterator = csvReader.iterator();

            PrintStream out = new PrintStream(new File(csvFilePath.getParentFile(), outFilenameString));
            if(ignoreFirstRow){
                String[] next = csvIterator.next();
                for(int i=0;i<next.length;i++){out.print(next[i]); if(i<next.length-1){out.print(",");}}
                out.println();
            }
            while (csvIterator.hasNext()) {
                row = csvIterator.next();
                int i=0;
                for(;i<colSize;i++){
                    float min = minimum[i];
                    float max = maximum[i];
                    float range = max - min;
                    float value = Float.parseFloat(row[i]);
                    float normalizedValue = (value - min)/range;
                    out.print(decimalformatter.format(normalizedValue));
                    if(i<(colSize-1))out.print(",");
                }
                if(ignoreLastColumn){out.print(","+row[i]);}
                out.println();
            }

            out.close();
        }catch(Exception ex){
            System.out.println("Count:"+count);
            ex.printStackTrace();
        }
    }
}
