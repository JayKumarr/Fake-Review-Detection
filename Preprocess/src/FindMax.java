
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class FindMax {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File("E:\\yelpResData\\NEW EXPERIMENTS\\6000\\nnc\\12 Features\\out_randomForest.txt")));
        
        String line = null;
        
        double maxPrecision = 0.0f;
        double maxRecall = 0.0f;
        double maxAccuracy = 0.0f;
        String cValue = "";
        String maxCvalue = "";
        while( (line = reader.readLine())!=null ){
            if(line.trim().startsWith("C=")){
                cValue = line;
            }
            if(line.trim().startsWith("TOTAL")){
                StringTokenizer tokenizer = new StringTokenizer(line,",");
                tokenizer.nextToken();
                String temp = tokenizer.nextToken();
                double precision = 0.0;
                if(!temp.trim().isEmpty())
                    precision = Double.parseDouble(temp);
                else{
                    precision = Double.parseDouble(tokenizer.nextToken());
                }
                
                
                temp = tokenizer.nextToken();
                double recall = 0.0;
                if(temp.trim().isEmpty())
                    recall = Double.parseDouble(tokenizer.nextToken());
                else{
                    recall = Double.parseDouble(temp);
                }
                if(maxPrecision<precision){
                    maxPrecision=precision;
                }
                if(maxRecall<recall){
                    maxRecall=recall;
                    maxCvalue = ""+cValue;
                    
                }
                String str = tokenizer.nextToken();
                double accuracy = 0.0;
                if(str.trim().isEmpty()){
                    accuracy = Double.parseDouble(tokenizer.nextToken());
                }else{
                    accuracy = Double.parseDouble(str);
                }
                if(maxAccuracy<accuracy){
                    maxAccuracy=accuracy;
                }
            }
        }
        
        System.out.println(maxCvalue);
        System.out.println("Precision = "+maxPrecision);
        System.out.println("Recall = "+maxRecall);
        System.out.println("Accuracy = "+maxAccuracy);
    }
}
