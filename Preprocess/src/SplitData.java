
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Workstation
 */
public class SplitData {
    public static int fake_start = 0;
    public static int true_start = 400;
    public static void main(String[] args) throws FileNotFoundException {
        String prefix = "unigram_BM25";
        File dir = new File("Z:\\op_spam_v1.4\\positive_polarity");
        File csvFile = new File(dir,prefix+".csv");
        for(;fake_start<400;fake_start+=40){
            int b = fake_start+40;
            File parent = new File(csvFile.getParent(),(fake_start+1)+"-"+b);
            parent.mkdir();
            for(true_start=400;true_start<800;true_start+=40){
                int d = true_start+40;
                File folder= new File(parent,(true_start+1)+"-"+d);
                folder.mkdir();
                PrintStream training_file = new PrintStream(new File(folder,"training_"+prefix+".csv"));
                PrintStream testing_file = new PrintStream(new File(folder,"testing_"+prefix+".csv"));
                Scanner scan = new Scanner(csvFile);
                int count = 0;
                while(scan.hasNext()){
                    String line = scan.nextLine();
                    if( (count>=fake_start&&count<b) || (count>=true_start&&count<d) ){
                        testing_file.println(line);
                    }else{
                        training_file.println(line);
                    }
                    count++;
                }
            }
        }
    }
}
