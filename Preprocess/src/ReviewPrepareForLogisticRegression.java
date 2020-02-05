
import com.yelp.model.ReviewPOJO;
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
import java.util.Set;

/*
 * This class takes two folder fake review folder and non-fake review folder
 * It will be export CSV file
 */
/**
 *
 * @author mf15-10
 */
public class ReviewPrepareForLogisticRegression {

    public static void generateUnigrams(File inputFolder, File outputFolder, boolean printColumns, boolean printOriginal, boolean printFakeColumn) throws IOException {

        File folderFakeReview = new File(inputFolder, "deceptive_from_MTurk");
        File folderTrueReview = new File(inputFolder, "truthful_from_TripAdvisor");
        StandfordLemmatizer lemmatizer = new StandfordLemmatizer();
        PrintStream outWords = new PrintStream(new FileOutputStream(new File(outputFolder, "uni_words.csv")));
        PrintStream out = new PrintStream(new FileOutputStream(new File(outputFolder, "unigram.csv")));

        Set<String> words = new HashSet<>();
        List<ReviewPOJO> fakeReviewsList = new ArrayList<>();
        List<ReviewPOJO> trueReviewsList = new ArrayList<>();

        ArrayList<File> fakeFileList = new ArrayList<>();
        getFiles(folderFakeReview, fakeFileList, ".txt");
        ArrayList<File> trueFileList = new ArrayList<>();
        getFiles(folderTrueReview, trueFileList, ".txt");

//        Loading review and tokenizing them
//        words having all distinct words 
        addingWordsIntoList(fakeFileList, fakeReviewsList, words, lemmatizer);
        addingWordsIntoList(trueFileList, trueReviewsList, words, lemmatizer);

        System.out.println("Total words: " + words.size());

//      assigning all words ID (index)
        Hashtable<String, Integer> table = new Hashtable();
        Hashtable<Integer, String> inversetable = new Hashtable();
        int index = 0;
        for (String word : words) {
            table.put(word, index);
            inversetable.put(index, word);
            index++;
        }

        int cols = words.size();

//      ~Print all words as ROW in CSV, remember first column is IS_FAKE
        if (printColumns) {
            if(printFakeColumn)out.print("IS_FAKE,");
            if (!printOriginal) {
                ArrayList<String> colsNames = ColumnNameGenerator.getColumns(words.size());
                for (int i = 0; i < words.size() - 1; i++) {
                    out.print(colsNames.get(i));
                    out.print(",");
                }
                out.print(colsNames.get(words.size() - 1));
            } else {
                for(int ID = 0; ID < inversetable.size(); ID++) {
                    String word = inversetable.get(ID);
                    out.print(word);
                    outWords.println(word);
                    if (ID < (inversetable.size() - 1)) {
                        out.print(",");
                    }
                    out.flush();
                }
            }
            out.println();
        }
//      in matrix row ID will be ID of reviewPOJO and column index is word index
//      e.g. matrix[5][359]= 4   =>  means ReviewPOJO who has ID = 5 
//      and word whose index is 359 in hashtable have occured four times in ReviewPOJO 5 
        int[][] matrix_fake_review = new int[fakeReviewsList.size()][cols];
        int[][] matrix_true_review = new int[trueReviewsList.size()][cols];

        int ID_counter = 0;

        ID_counter = addInMatrix(fakeReviewsList, matrix_fake_review, cols, table);
        printMatrixInFile(matrix_fake_review, out, "FAKE", cols,printFakeColumn);

        ID_counter = addInMatrix(trueReviewsList, matrix_true_review, cols, table);
        printMatrixInFile(matrix_true_review, out, "NONFAKE", cols,printFakeColumn);

        out.close();
        outWords.close();
    }

    private static int addInMatrix(List<ReviewPOJO> reviewsList, int[][] matrix_fake_review, int cols, Hashtable<String, Integer> table) {
        int ID_counter = 0;
        for (ReviewPOJO reviewPJO : reviewsList) {
            reviewPJO.setID(ID_counter);
            int[] vector = matrix_fake_review[ID_counter];
            for (int dimension = 0; dimension < cols; dimension++) {
                vector[dimension] = 0;
            }
            for (String word : reviewPJO.getLemmetized()) {
                Integer dimension = table.get(word);
                int temp = matrix_fake_review[reviewPJO.getID()][dimension] + 1;
                matrix_fake_review[reviewPJO.getID()][dimension] = temp;
            }
            ID_counter++;
        }
        return ID_counter;
    }

    private static void printMatrixInFile(int[][] matrix_review, PrintStream out, String is_fake, int cols, boolean printFakeColumn) {
        for (int ID = 0; ID < matrix_review.length; ID++) {
            int[] vector = matrix_review[ID];
            if(printFakeColumn)out.print(is_fake + ",");
            for (int innerIndex = 0; innerIndex < cols; innerIndex++) {
                out.print(matrix_review[ID][innerIndex]);
                if (innerIndex < (vector.length - 1)) {
                    out.print(",");
                }
            }
            out.println();
            out.flush();
        }
    }

    private static void getFiles(File file, ArrayList<File> list, String endsWith) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                getFiles(files[i], list, endsWith);
            }
        } else {
            if (endsWith != null && !endsWith.isEmpty()) {
                if (file.getName().toLowerCase().endsWith(endsWith)) {
                    list.add(file);
                }
            }
        }
    }

    private static void addingWordsIntoList(ArrayList<File> fileList, List<ReviewPOJO> reviewsList, Set<String> words, StandfordLemmatizer lemmatizer) throws IOException {
        for (File f : fileList) {
            String review = Helper.readFile(f).toLowerCase();
            List<String> lemmatize = lemmatizer.lemmatize(review,true);
            reviewsList.add(new ReviewPOJO(review, lemmatize));
            for (int i = 0; i < lemmatize.size(); i++) {
                String word = lemmatize.get(i);
                words.add(word);
            }
        }
    }
}
