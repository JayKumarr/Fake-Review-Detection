
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
public class SelectRandomReviews {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedReader in = new BufferedReader(new FileReader(new File("E:\\yelpResData\\NEW EXPERIMENTS\\TRUE_REVIEWS.csv")));
        PrintStream out = new PrintStream(new File("E:\\yelpResData\\NEW EXPERIMENTS\\TRUE_REVIEWS_1000.csv"));
        int counter = 0;
        out.println(in.readLine());
        while(counter<1030){
            String str = in.readLine();
            int random = (int)(Math.random()*5);
            for(int i=1;i<random;i++){
                str = in.readLine();
            }
            out.println(str);
            counter++;
        }
        in.close();
        out.close();
    }
}
