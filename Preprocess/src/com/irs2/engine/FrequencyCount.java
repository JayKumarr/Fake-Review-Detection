/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.irs2.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class FrequencyCount {
    public static HashMap<String,Integer> calculateDF(List<String> docs){
        HashMap<String,Integer> mapTerm = new HashMap<String, Integer>();//<term, DF>
        docs.stream().forEach( (documentText)->{
            List<String> termList = Similarity.lemmatize(documentText);
            HashSet<String> distinctWords = new HashSet<String>(termList);
            distinctWords.stream().forEach((term)->{
                Integer df = mapTerm.get(term);
                if(df == null){
                    mapTerm.put(term,1);
                }else{
                    df = df+1;
                    mapTerm.put(term,df);
                }
            });
        });
        return mapTerm;
    }
    
    public static HashMap<String,Float> calculateIDF(List<String> docs){
        HashMap<String,Float> mapTermIDF = new HashMap<String, Float>();//<term, IDF>
        int N = docs.size();
        HashMap<String, Integer> calculateDF = calculateDF(docs);
        for (Map.Entry<String, Integer> entrySet : calculateDF.entrySet()) {
            String term = entrySet.getKey();
            Integer DF = entrySet.getValue();
            float idf = (float) Math.log(N/DF);
            mapTermIDF.put(term, idf);
        }
        
        return mapTermIDF;
    }
}
