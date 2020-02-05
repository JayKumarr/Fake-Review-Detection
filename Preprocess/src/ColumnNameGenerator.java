
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author workstation-pc
 */
public class ColumnNameGenerator {

    static int i = 65;
    static int j = 65;
    static int k = 65;
    static int l = 65, m = 65, n = 65, o = 65, p = 65, q = 65, r = 65;
    static int previous_count= 0;
    public static void main(String arg[]) throws FileNotFoundException, IOException {
        
        File inputFolder = new File("E:\\op_spam_v1.4 (Myle Ott)\\positive_polarity");
        File outputFolder = new File("E:\\op_spam_v1.4 (Myle Ott)\\positive_polarity");
        
//        ReviewPrepareForLogisticRegressionBigram.generateUnigrams(inputFolder, outputFolder, true, false,true);
//        ReviewPrepareForLogisticRegression.generateUnigrams(inputFolder, outputFolder, true, false,true);
        ReviewPrepareUnigramForSVMwithBM25.generateUnigrams(inputFolder, outputFolder, true, false, true);
        
//        int limit = 39574;
//        ArrayList<String> list =getColumns(limit);
//        PrintStream out = new PrintStream(new File("C:\\Users\\farooq\\Desktop\\Output\\unigram_bigram.csv"));
//        int index = 0;
//        out.print("IS_FAKE");
//        out.print(",");
//        for (; index < list.size() - 1; index++) {
//            out.print(list.get(index));
//            out.print(",");
//        }
//        out.print(list.get(index));
//        out.println();
//        Scanner scanUnigram = new Scanner(new File("C:\\Users\\farooq\\Desktop\\Output\\unigram.csv"));
//        Scanner scanBigram = new Scanner(new File("C:\\Users\\farooq\\Desktop\\Output\\bigram.csv"));
//
//        String line = scanBigram.nextLine();
//        line = scanUnigram.nextLine();
//        while (scanBigram.hasNext()) {
//            out.println(scanBigram.nextLine() + "," + scanUnigram.nextLine());
//            out.flush();
//        }
//
//        out.close();
//        scanBigram.close();
//        scanUnigram.close();
//        System.out.println("list size:" + list.size());

    }
    
    public static ArrayList<String> getColumns(int limit){
        ArrayList<String> list = new ArrayList<>();
        int countIteration = 0;
        for (i=65; i <= 90; i++) {
            char c = (char) i;
            countIteration++;
            if(countIteration>previous_count)list.add("" + c);
            if (limit <= list.size()) {
                break;
            }
        }

        label1:
        for (j=65; j <= 90; j++) {
            char c = (char) j;
            for (k=65; k <= 90; k++) {
                char d = (char) k;
                countIteration++;
                if(countIteration>previous_count)list.add(c + "" + d);
                if (limit <= list.size()) {
                    break label1;
                }
            }
        }

        label2:
        for (l = 65; l <= 90; l++) {
            char c = (char) l;
            for (m = 65; m <= 90; m++) {
                char d = (char) m;
                for (n = 65; n <= 90; n++) {
                    char e = (char) n;
                    countIteration++;
                    if(countIteration>previous_count)list.add(c + "" + d + "" + e);
                    if (limit <= list.size()) {
                        break label2;
                    }
                }
            }
        }

        label3:
        for (o = 65; o <= 90; o++) {
            char c = (char) o;
            for (p = 65; p <= 90; p++) {
                char d = (char) p;
                for (q=65; q <= 90; q++) {
                    char e = (char) q;
                    for (r=65; r <= 90; r++) {
                        char f = (char) r;
                        countIteration++;
                        if(countIteration>previous_count)list.add(c + "" + d + "" + e + "" + f);
                        if (limit <= list.size()) {
                            break label3;
                        }
                    }
                }
            }
        }
        previous_count+=limit;
        return list;
    }
}
