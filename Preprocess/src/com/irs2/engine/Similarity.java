/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.irs2.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Java version 1.8
 * @author Administrator
 */
public class Similarity {
    private static StandfordLemmatizer lemmetizer = new StandfordLemmatizer();
    /**
     * @param lemmatizedVect1
     * @param lemmatizedVect2
     * @return score
     * NNC = see table in IRS lecture#3 slide 45
     */    
    public static double cosineSimilarityNNC(List<String> lemmatizedVect1, List<String> lemmatizedVect2) {
        HashSet<String> vocab = new HashSet<>();
        vocab.addAll(lemmatizedVect1);
        vocab.addAll(lemmatizedVect2);
        HashMap<String, Double> invertedIndexVect1 = new HashMap<>();//<term, TF>
        HashMap<String, Double> invertedIndexVect2 = new HashMap<>();
        vocab.stream().forEach( (term) -> {
            invertedIndexVect1.put(term, 0d);
            invertedIndexVect2.put(term, 0d);
        });
        lemmatizedVect1.stream().forEach((term) -> {
            double tf = invertedIndexVect1.get(term);
            tf = tf + 1;
            invertedIndexVect1.put(term, tf);
        });
        lemmatizedVect2.stream().forEach( (term) ->{
            double tf = invertedIndexVect2.get(term);
            tf = tf + 1;
            invertedIndexVect2.put(term, tf);
        });
        long sumV1 =0;
        long sumV2 =0;
        Iterator<Double> it = invertedIndexVect1.values().iterator();
        Iterator<Double> it2 = invertedIndexVect2.values().iterator();
        while(it.hasNext()){
            double tf = it.next();
            sumV1 += (tf*tf);
            
            double tfV2 = it2.next();
            sumV2 += (tfV2*tfV2);
        }
        double denominator_vector1 = Math.sqrt(sumV1);
        double denominator_vector2 = Math.sqrt(sumV2);
        
        for (Map.Entry<String, Double> entrySet : invertedIndexVect1.entrySet()) {
            String key = entrySet.getKey();
            Double tf = entrySet.getValue();
            double normalized = tf/denominator_vector1;
            invertedIndexVect1.put(key, normalized);
        }
        double similarity = 0.0d;
        for (Map.Entry<String, Double> entrySet : invertedIndexVect2.entrySet()) {
            String key = entrySet.getKey();
            Double tf = entrySet.getValue();
            double normalized = tf/denominator_vector2;
            invertedIndexVect2.put(key, normalized);
            similarity += (normalized*invertedIndexVect1.get(key));
        }
        return similarity;
    }

    /**
     * 
     * @param lemmatizedVect1
     * @param lemmatizedVect2
     * @param termIDF
     * @return 
     * LTC = see table in IRS lecture#3 slide 45
     */
    public static double cosineSimilarityLTC(List<String> lemmatizedVect1, List<String> lemmatizedVect2, HashMap<String,Float> termIDF) {
        HashSet<String> vocab = new HashSet<>();
        vocab.addAll(lemmatizedVect1);
        vocab.addAll(lemmatizedVect2);
        HashMap<String, Float> invertedIndexVect1 = new HashMap<>();
        HashMap<String, Float> invertedIndexVect2 = new HashMap<>();
        vocab.stream().forEach( (term) -> {
            invertedIndexVect1.put(term, 0f);
            invertedIndexVect2.put(term, 0f);
        });
        lemmatizedVect1.stream().forEach((term) -> {
            float tf = invertedIndexVect1.get(term);
            tf = tf + 1;
            invertedIndexVect1.put(term, tf);
        });
        lemmatizedVect2.stream().forEach( (term) ->{
            float tf = invertedIndexVect2.get(term);
            tf = tf + 1;
            invertedIndexVect2.put(term, tf);
        });
        
        //For calculating TFIDF LTC
        for (Map.Entry<String, Float> entrySet : invertedIndexVect1.entrySet()) {
            String term = entrySet.getKey();
            Float tf = entrySet.getValue();
            Float IDF = termIDF.get(term);
            float tfidf = (float)(tf*IDF);
            invertedIndexVect1.replace(term, tfidf);
        }
        for (Map.Entry<String, Float> entrySet : invertedIndexVect2.entrySet()) {
            String term = entrySet.getKey();
            Float tf = entrySet.getValue();
            Float IDF = termIDF.get(term);
            float tfidf = (float)(tf*IDF);
            invertedIndexVect2.replace(term, tfidf);
        }
        long sumV1 =0;
        long sumV2 =0;
        Iterator<Float> it = invertedIndexVect1.values().iterator();
        Iterator<Float> it2 = invertedIndexVect2.values().iterator();
        // calculating square and sum 
        while(it.hasNext()){
            double tfidf = it.next();
            sumV1 += (tfidf*tfidf);
            
            double tfidfV2 = it2.next();
            sumV2 += (tfidfV2*tfidfV2);
        }
        double denominator_vector1 = Math.sqrt(sumV1);
        double denominator_vector2 = Math.sqrt(sumV2);
        
        for (Map.Entry<String, Float> entrySet : invertedIndexVect1.entrySet()) {
            String key = entrySet.getKey();
            Float tfidf = entrySet.getValue();
            float normalized = (float)(tfidf/denominator_vector1);
            invertedIndexVect1.put(key, normalized);
        }
        double similarity = 0.0d;
        for (Map.Entry<String, Float> entrySet : invertedIndexVect2.entrySet()) {
            String key = entrySet.getKey();
            Float tfidf = entrySet.getValue();
            double normalized = tfidf/denominator_vector2;
            invertedIndexVect2.put(key, (float)normalized);
            similarity += (normalized*invertedIndexVect1.get(key));
        }
        return similarity;
    }
    
    public static double cosineSimilarityBM25(List<String> lemmatizedVect1, List<String> lemmatizedVect2, HashMap<String,Float> termIDF, float k) {
        HashSet<String> vocab = new HashSet<>();
        vocab.addAll(lemmatizedVect1);
        vocab.addAll(lemmatizedVect2);
        HashMap<String, Float> invertedIndexVect1 = new HashMap<>();
        HashMap<String, Float> invertedIndexVect2 = new HashMap<>();
        vocab.stream().forEach( (term) -> {
            invertedIndexVect1.put(term, 0f);
            invertedIndexVect2.put(term, 0f);
        });
        lemmatizedVect1.stream().forEach((term) -> {
            float tf = invertedIndexVect1.get(term);
            tf = tf + 1;
            invertedIndexVect1.put(term, tf);
        });
        lemmatizedVect2.stream().forEach( (term) ->{
            float tf = invertedIndexVect2.get(term);
            tf = tf + 1;
            invertedIndexVect2.put(term, tf);
        });
        
        //For calculating BM25
        for (Map.Entry<String, Float> entrySet : invertedIndexVect1.entrySet()) {
            String term = entrySet.getKey();
            Float tf = entrySet.getValue();
            Float IDF = termIDF.get(term);
            float tfidf = (float)(tf*( ((k+1)*tf) / (tf+k) )*IDF);
            invertedIndexVect1.replace(term, tfidf);
        }
        
        for (Map.Entry<String, Float> entrySet : invertedIndexVect2.entrySet()) {
            String term = entrySet.getKey();
            Float tf = entrySet.getValue();
            Float IDF = termIDF.get(term);
            float tfidf = (float)(tf*( ((k+1)*tf) / (tf+k) )*IDF);
            invertedIndexVect2.replace(term, tfidf);
        }
        
        long sumV1 =0;
        long sumV2 =0;
        Iterator<Float> it = invertedIndexVect1.values().iterator();
        Iterator<Float> it2 = invertedIndexVect2.values().iterator();
        while(it.hasNext()){
            double tf = it.next();
            sumV1 += (tf*tf);
            
            double tfV2 = it2.next();
            sumV2 += (tfV2*tfV2);
        }
        double denominator_vector1 = Math.sqrt(sumV1);
        double denominator_vector2 = Math.sqrt(sumV2);

        //Normalization
        for (Map.Entry<String, Float> entrySet : invertedIndexVect1.entrySet()) {
            String key = entrySet.getKey();
            Float tf = entrySet.getValue();
            float normalized = (float)(tf/denominator_vector1);
            invertedIndexVect1.put(key, normalized);
        }
        double similarity = 0.0d;
        for (Map.Entry<String, Float> entrySet : invertedIndexVect2.entrySet()) {
            String key = entrySet.getKey();
            Float tf = entrySet.getValue();
            double normalized = tf/denominator_vector2;
            invertedIndexVect2.put(key, (float)normalized);
            similarity += (normalized*invertedIndexVect1.get(key));
        }
        return similarity;
    }
    
    public static List<String> lemmatize(String str){
        return lemmetizer.lemmatize(str,true);
    }
    
    
    
}
