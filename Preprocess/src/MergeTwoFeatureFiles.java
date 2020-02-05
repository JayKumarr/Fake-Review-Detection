
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class MergeTwoFeatureFiles {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File csvFilePath = new File("E:\\yelpResData\\NEW EXPERIMENTS\\2000\\ltc\\5BF_Unigram\\5BF_REVIEWS_LTC.csv");
        File csvFilePath2 = new File("E:\\yelpResData\\NEW EXPERIMENTS\\2000\\ltc\\5BF_Unigram\\unigram_data_for_svm.csv");
        BufferedReader inStart = new BufferedReader(new FileReader(csvFilePath));
        BufferedReader inEnd = new BufferedReader(new FileReader(csvFilePath2));
        File outputFile = new File(csvFilePath.getParentFile(),"5BF+Unigram_REVIEWS_LTC.csv");
        if(outputFile.exists()){
            System.out.print("outputFile already exists, do you want to override it? y/n : ");
            String input = (new Scanner(System.in)).next();
            if(!input.toLowerCase().startsWith("y")){
                System.out.println("Process terminated..!");
                System.exit(0);
            }
        }
        PrintStream out = new PrintStream(new FileOutputStream(outputFile));
        String line = null;
        while ((line=inStart.readLine())!=null){
            String endLine = inEnd.readLine();
            out.print(line);
            out.print(",");
            out.println(endLine);
        }
        out.close();
        inStart.close();
        inEnd.close();
    }
}
