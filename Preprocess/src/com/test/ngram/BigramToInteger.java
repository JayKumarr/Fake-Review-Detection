package com.test.ngram;


import com.irs.utils.JTime;
import com.irs2.engine.JTokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class BigramToInteger {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File f = new File("D:\\yelpResData\\experiment_2000\\review_content.txt");
        PrintStream out = new PrintStream(new File(f.getParentFile(),"bigram_data_for_svm.csv"));
        PrintStream outVocab = new PrintStream(new File(f.getParentFile(),"bigram_vocab.csv"));
        BufferedReader reader = new BufferedReader(new FileReader(f));
        HashMap<String, Integer> vocabulary = new HashMap<>();
        HashMap<Integer,String> vocabularyInverse = new HashMap<>();
//        HashMap<String,String> vocabLineNumber = new HashMap<>();
        String str = null;
        ArrayList<ArrayList<Integer>> fileData = new ArrayList<>();
        JTime time = new JTime();
        time.startTime();
//        int lineNumber = 1;
        while( (str = reader.readLine())!= null ){
            List<String> tokens = JTokenizer.getTokens(str, null, false,true);
            ArrayList<Integer> tokenIds = new ArrayList<Integer>();
             for(int j=0;j<tokens.size()-1;j++){
                String term = tokens.get(j);
                term = (term+" "+tokens.get(j+1));
                Integer indexID = vocabulary.get(term);
                if(indexID==null){
                    indexID = vocabulary.size()+1;
                    vocabulary.put(term, indexID);
                    vocabularyInverse.put(indexID,term);
//                    vocabLineNumber.put(term, "");
                }
                tokenIds.add(indexID);
//                String pos = vocabLineNumber.get(term)+","+lineNumber;
//                vocabLineNumber.put(term, pos);
                
            }
            fileData.add(tokenIds);
//            fileData.add(tokens);
//            lineNumber++;
//            if(lineNumber%100==0){
//                System.out.println(time.intervalBreak(JTime.SECONDS)+" Seconds");
//            }
        }//end while
        System.out.println("All vocabulary is collected..!");
        System.out.println(time.interval(JTime.MINTUES_AND_SECONDS));
        reader.close();
        for(int index=0; index<fileData.size();index++){
            List<Integer> tokens = fileData.get(index);
            HashMap<Integer, Integer> termCountMap = new HashMap<>();
            for(Integer termID: tokens){
//                Integer termID = vocabulary.get(term);
                if(!termCountMap.containsKey(termID)){
                    termCountMap.put(termID, 0);
                }
                int count = termCountMap.get(termID);
                count++;
                termCountMap.put(termID, count);
            }
            for (Map.Entry<String, Integer> entry : vocabulary.entrySet()) {
                String key = entry.getKey();
                Integer vocTermID = entry.getValue();

                out.print(",");
                if(termCountMap.containsKey(vocTermID)){
                    out.print(termCountMap.get(vocTermID));
                }else{
                    out.print("0");
                }
            }
            out.println();
            out.flush();
        }
        out.close();
        
        for (Map.Entry<String, Integer> entry : vocabulary.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            outVocab.println(value+","+key+",");
            outVocab.flush();
        }
        outVocab.close();
        System.out.println(time.interval(JTime.MINTUES_AND_SECONDS));
    }
    

}
