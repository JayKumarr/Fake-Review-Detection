
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mf15-10
 */
public class cbind {
    public static void main(String arg[]) throws FileNotFoundException{
        File parent = new File("E:\\op_spam_v1.4 (Myle Ott)\\positive_polarity");
        Scanner scan1 = new Scanner(new File(parent,"unigram.csv"));
        Scanner scan2 = new Scanner(new File(parent,"bigram.csv"));
        PrintStream out = new PrintStream(new FileOutputStream(new File(parent,"unigram+bigram_for_python.csv")));
        
        while(scan1.hasNext()){
            out.print(scan1.nextLine());
            out.print(",");
            out.println(scan2.nextLine());
        }
        
        out.close();
        scan1.close();
        scan2.close();
    }
}
