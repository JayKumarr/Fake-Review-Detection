
import com.yelp.model.ReviewPOJO;
import com.irs.utils.Utility;
import com.irs2.engine.Helper;
import com.irs2.engine.StandfordLemmatizer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * This class takes two folder fake review folder and non-fake review folder
 * It will be export CSV file
 */

/**
 *
 * @author mf15-10
 */
public class ReviewPrepareForLogisticRegressionTFIDF {
    public static void main(String[] args) throws IOException {
        File inputFolder = new File("\\\\fs\\mf15$\\mf15-10\\op_spam_v1.4\\positive_polarity");
        File outputFolder = new File("C:\\Users\\farooq\\Desktop\\Output");
        
        File folderFakeReview = new File(inputFolder,"deceptive_from_MTurk");
        File folderTrueReview = new File(inputFolder,"truthful_from_TripAdvisor");
        StandfordLemmatizer lemmatizer = new StandfordLemmatizer();
        PrintStream outWords = new PrintStream(new FileOutputStream(new File(outputFolder,"unigram_words.csv")));
        PrintStream out = new PrintStream(new FileOutputStream(new File(outputFolder,"unigram_TFIDF.csv")));
        
        Set<String> words = new HashSet<>();
        Hashtable<String, Integer> df = new Hashtable<>(); // <Term, DF>
        Hashtable<Integer, Double> idf = new Hashtable<>(); // <TermID, IDF>
        
        List<ReviewPOJO> fakeReviewsList = new ArrayList<>();
        List<ReviewPOJO> trueReviewsList = new ArrayList<>();
        
        ArrayList<File> fakeFileList = new ArrayList<>();
        getFiles(folderFakeReview, fakeFileList, ".txt");
        ArrayList<File> trueFileList = new ArrayList<>();
        getFiles(folderTrueReview, trueFileList, ".txt");

//        Loading review and tokenizing them
//        words having all distinct words 
        addingWordsIntoList(fakeFileList, fakeReviewsList, words, lemmatizer, df);
        addingWordsIntoList(trueFileList, trueReviewsList, words, lemmatizer, df);
        
        System.out.println("Total words: "+words.size()+"   df size: "+df.size());
        
//      assigning all words ID (index) and calculating IDF
        Hashtable<String, Integer> table = new Hashtable();
        Hashtable<Integer, String> inversetable = new Hashtable();
        int index = 0;
        for(String word: words){
            table.put(word, index);
            inversetable.put(index, word);
            index++;
        }
        
        int cols = words.size();
        
        // <editor-fold defaultstate="collapsed" desc="Print all words as ROW in CSV, remember first column is IS_FAKE">
        out.print("IS_FAKE,");
        for(int ID=0;ID<inversetable.size();ID++){
            String word = inversetable.get(ID);
            out.print(word);
            outWords.println(word);
            if(ID < (inversetable.size()-1)){
                out.print(",");
            }
            out.flush();
        }
        out.println();
//        </editor-fold>    
        
        // <editor-fold defaultstate="collapsed" desc="Calculating IDF">
        for (Map.Entry<String, Integer> entrySet : df.entrySet()) {
            String word = entrySet.getKey();
            Integer wordDF = entrySet.getValue();            
            int termID = table.get(word);
            idf.put(termID, Utility.log(((trueReviewsList.size()+fakeReviewsList.size())/wordDF ),2) );
        }
//        </editor-fold>
        
//      in matrix row ID will be ID of reviewPOJO and column index is word index
//      e.g. matrix[5][359]= 4   =>  means ReviewPOJO who has ID = 5 
//      and word whose index is 359 in hashtable have occured four times in ReviewPOJO 5 
        int[][] matrix_fake_review = new int[fakeReviewsList.size()][cols];        
        int[][] matrix_true_review = new int[trueReviewsList.size()][cols];        
        
        int ID_counter = 0;

        ID_counter = addInMatrix(fakeReviewsList, matrix_fake_review, cols, table);
        printMatrixInFileWithTFIDF(matrix_fake_review, out,1, cols, idf);
    
        ID_counter = addInMatrix(trueReviewsList, matrix_true_review, cols, table);
        printMatrixInFileWithTFIDF(matrix_true_review, out,0, cols, idf);
        
        out.close();
        outWords.close();
    }
    
    
    public static int addInMatrix(List<ReviewPOJO> reviewsList,int[][] matrix_fake_review, int cols, Hashtable<String, Integer> table){
        int ID_counter  = 0;
        for(ReviewPOJO reviewPJO : reviewsList){
            reviewPJO.setID(ID_counter);
            int[] vector = matrix_fake_review[ID_counter];
            for(int dimension = 0; dimension<cols; dimension++){
                vector[dimension] = 0;
            }
            for(String word: reviewPJO.getLemmetized()){
                Integer dimension =  table.get(word);
                int temp = matrix_fake_review[reviewPJO.getID()][dimension]+1;
                matrix_fake_review[reviewPJO.getID()][dimension] = temp;
            }
            ID_counter++;
        }
        return ID_counter;
    }
        
    public static void printMatrixInFileWithTFIDF(int[][] matrix_review, PrintStream out, int is_fake, int cols, Hashtable<Integer, Double> idf){
        for(int ID=0; ID <matrix_review.length; ID++){
            int[] vector = matrix_review[ID];
            out.print(is_fake+",");
            for(int termID=0;termID<cols; termID++){
                int tf = matrix_review[ID][termID];
                if(tf == 0){
                    out.print(tf);
                }else{
                    double tfidf = (1+Utility.log(tf,2))* idf.get(termID) ;
                    out.print(tfidf);

                }
                if(termID < (vector.length-1) ){
                    out.print(",");
                }                
            }
            out.println();
            out.flush();
        }
    }
    
    public static void getFiles(File file, ArrayList<File> list, String endsWith){
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(int i=0; i<files.length;i++){
                getFiles(files[i], list, endsWith);
            }
        }else{
            if(endsWith != null && !endsWith.isEmpty()){
                if(file.getName().toLowerCase().endsWith(endsWith)){
                    list.add(file);
                }
            }
        }
    }
    
    
    private static void addingWordsIntoList(ArrayList<File> fileList, List<ReviewPOJO> reviewsList, Set<String> words, StandfordLemmatizer lemmatizer, Hashtable<String, Integer> df) throws IOException {
        for(File f : fileList){
            String review = Helper.readFile(f).toLowerCase();
            List<String> lemmatize = lemmatizer.lemmatize(review,true);            
            reviewsList.add(new ReviewPOJO(review, lemmatize));
            HashSet<String> distinctTerms = new HashSet<>();
            for(int i=0; i< lemmatize.size(); i++){
                String word = lemmatize.get(i);
                distinctTerms.add(word);
                boolean b =words.add(word);
                if(b){
                    df.put(word, 0);
                }
            }
            for(String term : distinctTerms){
                int termDF = df.get(term);
                df.put(term, termDF+1);
            }
        }
    }
}
