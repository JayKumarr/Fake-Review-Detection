
import com.opencsv.CSVReader;
import com.yelp.database.SQliteConnection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class ExtractFeatureFromDbByID {
    public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {
        Connection connection = SQliteConnection.getInstance().getConnection();
        File csvFilePath = new File("E:\\yelpHotelData\\All_Reviews.csv");
        String outFilenameString = "reviewerID";
        CSVReader csvReader = new CSVReader(new FileReader(csvFilePath));
        PrintStream out = new PrintStream(new File(csvFilePath.getParentFile(), outFilenameString));
        String[] row = csvReader.readNext();// this is column names
        System.out.println(outFilenameString);
        int lineNumber = 0;
        while ((row = csvReader.readNext()) != null) {
            if(row.length != 22){System.out.println(row[1]);break;}
            String reviewID = row[1];
            String query = "SELECT r.reviewerID from review r WHERE r.reviewID = '"+reviewID+"'";

                try (
                    
                    ResultSet executeQuery = connection.createStatement().executeQuery(query)) {
                if(executeQuery.next()){
                    String aFloat = executeQuery.getString("reviewerID");
                    out.println(aFloat);
                    out.flush();
                }else{
//                    System.err.println("reviewID "+reviewID+"   not found");
                    System.out.println(query);
                }
            }
        }
        out.close();
        csvReader.close();
    }
}
