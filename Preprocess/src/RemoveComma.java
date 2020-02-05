
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class RemoveComma {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File csvFilePath = new File("E:\\yelpResData\\NEW EXPERIMENTS\\unigram_data_for_svm.csv");
        BufferedReader in = new BufferedReader(new FileReader(csvFilePath));
        PrintStream out = new PrintStream(new File(csvFilePath.getParentFile(),csvFilePath.getName()+"_removedComma"));
        String str = null;
        while( (str = in.readLine())!=null ){
            String temp = str.substring(1);
            out.println(temp);
        }
        in.close();
        out.close();
    }
}
