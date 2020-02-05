
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
public class MergeFakeAndTrueFiles {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File csvFilePath = new File("E:\\yelpHotelData\\FAKE_REVIEWS_HOTEL.csv");
        File csvFilePath2 = new File(csvFilePath.getParentFile(),"TRUE_REVIEWS_HOTEL.csv");
        BufferedReader inStart = new BufferedReader(new FileReader(csvFilePath));
        BufferedReader inEnd = new BufferedReader(new FileReader(csvFilePath2));
        File outputFile = new File(csvFilePath.getParentFile(),"All_Reviews.csv");
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
            out.println(line);
        }
        while ((line=inEnd.readLine())!=null){
            out.println(line);
        }
        out.close();
        inStart.close();
        inEnd.close();
    }

}
