package com.irs2.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class JTokenizer {

    private static String tokenParams = "[\"':;.,-!*~\\/()0123456789_$%^?<>{}]+ ";
    private static Set<String> stopWords = new LinkedHashSet<>(Arrays.asList("a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing", "don't", "down", "during", "each", "few", "for", "from", "further", "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves"));
    private static StandfordLemmatizer lemmatizer = new StandfordLemmatizer();

    public static List<String> getTokens(String str, String fileName, boolean ignoreStopWords, boolean lemmatize) {
        List<String> tokenSet = new ArrayList<>();
        str = str.toLowerCase();
        if (lemmatize) {
            List<String> strList = lemmatizer.lemmatize(str.toLowerCase(), true);
            for (int i = 0; i < strList.size(); i++) {
                StringTokenizer tokenizer = new StringTokenizer(strList.get(i), tokenParams);
                /*Split into tokens and remove all stop-words*/
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    if (ignoreStopWords && (token.length() < 3 || stopWords.contains(token)) /*** * || tokenSet.contains(token)*/) {
                        continue;
                    }
                    tokenSet.add(token);
                }
            }
        } else {
            StringTokenizer tokenizer = new StringTokenizer(/*strList.get(i)*/str, tokenParams);
            /*Split into tokens and remove all stop-words*/
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                if (!ignoreStopWords && (token.length() < 3 || stopWords.contains(token)) /**
                         * || tokenSet.contains(token)
                         */
                        ) {
                    continue;
                }
//	    		if(token.length()>16){
//	    			System.out.println(token+ " > "+ fileName);
//	    			File fd = new File(fileName);
//	    			if(fd.exists()){
//	    				fd.delete();
//	    			}
//	    			
//	    		}
                tokenSet.add(token);
            }
        }
        return tokenSet;
    }

}
