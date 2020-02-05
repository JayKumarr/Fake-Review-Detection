
import java.io.BufferedReader;
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
public class TempUniqueRecords {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File filePath = new File("E:\\yelpHotelData\\reviewerID");
        HashSet<String> set = new HashSet<String>();
        BufferedReader in = new BufferedReader(new FileReader(filePath));
        String str = null;
        while( (str = in.readLine())!=null ){
            String temp = str.substring(1);
            set.add(temp);
        }
        in.close();
        System.out.println("size: "+set.size());
    }
}
