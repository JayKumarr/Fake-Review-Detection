
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class Temp {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File csvFilePath = new File("E:\\yelpHotelData\\Experiments\\bm\\All_Reviews - BM.csv");
        CSVReader csvReader = new CSVReader(new FileReader(csvFilePath));
        String[] row = null;
        
        int columns = 13;
        csvReader.readNext();
        int rowNumber=1;
        while ((row = csvReader.readNext()) != null) {
            rowNumber++;
            if(row.length!=columns){
                System.err.println(row);
                continue;
            }
            int i=0;
            try{
            for( ;i< (columns-1);i++ ){   //(columns-1) <- because last column is flagged
                Float.parseFloat(row[i]);
            }
            }catch(Exception ex){
                ex.printStackTrace();
                System.err.println("CSV Line Number: "+rowNumber+","+i);
                break;
                
            }
        }
        
    }
}
