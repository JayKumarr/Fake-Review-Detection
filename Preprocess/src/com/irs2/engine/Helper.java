package com.irs2.engine;

import com.irs.utils.Utility;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

public class Helper {

    
    private static String DELIMETER="~=";
    
    public static String readFile(File file) throws FileNotFoundException, IOException{
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);
        String str = null;
        StringBuilder builder = new StringBuilder();
        while( (str=reader.readLine()) != null  ){
            builder.append(str);
        }
        reader.close();
        fileReader.close();
        return builder.toString();
    }
    
    public static void putDocInFile(HashMap<Integer, JDoc> map, File file) throws IOException{
    	DataOutputStream dosVocab  = new DataOutputStream(new FileOutputStream(file));
        Iterator<Integer> it = map.keySet().iterator();
        while(it.hasNext()){
        	int key = it.next();
        	dosVocab.writeBytes(getKeyValue(key, map.get(key)));
        	dosVocab.writeBytes("\n");
        	dosVocab.flush();
        }
        dosVocab.close();
    }
    
    public static void putVocabInFile(HashMap<String, Object> map, File file) throws IOException{
    	DataOutputStream dosVocab  = new DataOutputStream(new FileOutputStream(file));
        Iterator<String> it = map.keySet().iterator();
        while(it.hasNext()){
        	String key = it.next();
        	dosVocab.writeBytes(getKeyValue(key, map.get(key)));
        	dosVocab.writeBytes("\n");
        	dosVocab.flush();
        }
        dosVocab.close();
    }
    
   

    public static void saveInFile(String str, File file) throws IOException{
    	DataOutputStream dosVocab  = new DataOutputStream(new FileOutputStream(file));
        	dosVocab.writeBytes(str);
        	dosVocab.flush();
        	dosVocab.close();
    }
    
    public static void savePostings(LinkedHashMap<Integer,LinkedHashMap<Integer,Integer>> termPostingsMap, File f) throws IOException{
    	DataOutputStream dosVocab  = new DataOutputStream(new FileOutputStream(f));
        Iterator<Integer> it = termPostingsMap.keySet().iterator();
        while(it.hasNext()){
        	Integer termID = it.next();
        	dosVocab.writeBytes(termID+"=");
        	LinkedHashMap<Integer, Integer> tpm = termPostingsMap.get(termID);
        	Iterator<Integer> postingIt = tpm.keySet().iterator();
        	while(postingIt.hasNext()){
        		int docId = postingIt.next();
        		int tf = tpm.get(docId);
        		String str = "["+docId+":"+tf+"],";
            	dosVocab.writeBytes(str);
        	}
        	dosVocab.writeBytes("\n");
        	dosVocab.flush();
        }
        dosVocab.close();
    }
    
    public static StringBuilder savePosting_TFIDF(LinkedHashMap<Integer,LinkedHashMap<Integer,Integer>> termPostingsMap, int N, File postingFile, File tfidfFile) throws IOException{
    	DataOutputStream dosVocab  = new DataOutputStream(new FileOutputStream(postingFile));
    	DataOutputStream dostfidf  = new DataOutputStream(new FileOutputStream(tfidfFile));
    	
    	Iterator<Integer> termIDs = termPostingsMap.keySet().iterator();
    	StringBuilder sbWeightSum = new StringBuilder();
    	/*traversing every term having doc_ids and ther term frequency to calcule tf.idf*/
    	while(termIDs.hasNext()){
    		int term_id = termIDs.next();
    		LinkedHashMap<Integer,Integer> docID_tf_map = termPostingsMap.get(term_id);
    		int df = docID_tf_map.size();
    		double termWeightSum = 0.0d;
    		
    		dostfidf.writeBytes(term_id+"=");
    		dostfidf.flush();

    		dosVocab.writeBytes(term_id+"=");
    		dosVocab.flush();
    		
    		Iterator<Integer> termDocIdsIterator = docID_tf_map.keySet().iterator();
    		while(termDocIdsIterator.hasNext()){
    			int docID = termDocIdsIterator.next();
    			int tf = docID_tf_map.get(docID);
    			
    			double tf_idf = ( (1+  (Utility.log(tf, 2))) *   (Utility.log( ((double) N/ (double)df), 2)) );
    			termWeightSum += tf_idf;
    			dostfidf.writeBytes("["+docID+":"+tf_idf+"],");
    			dostfidf.flush();
    			
    			String str = "["+docID+":"+tf+"],";
            	dosVocab.writeBytes(str);
            	dosVocab.flush();
    		}//end while
    		dostfidf.writeBytes("\n");
    		dostfidf.flush();
    		dosVocab.writeBytes("\n");
        	dosVocab.flush();
    		sbWeightSum.append(term_id+"="+termWeightSum+"\n");
    	}// end while

    	dostfidf.close();
    	dosVocab.close();
    	
    	return sbWeightSum;
    }

    
    public static StringBuilder savePosting_TFIDF_BM25(LinkedHashMap<Integer,LinkedHashMap<Integer,Integer>> termPostingsMap, int N, File postingFile, File tfidfFile, File bm25File) throws IOException{
    	DataOutputStream dosVocab  = new DataOutputStream(new FileOutputStream(postingFile));
    	DataOutputStream dostfidf  = new DataOutputStream(new FileOutputStream(tfidfFile));
    	DataOutputStream dosbm25  = new DataOutputStream(new FileOutputStream(bm25File));

    	
    	Iterator<Integer> termIDs = termPostingsMap.keySet().iterator();
    	StringBuilder sbWeightSum = new StringBuilder();
    	/*traversing every term having doc_ids and ther term frequency to calcule tf.idf*/
    	while(termIDs.hasNext()){
    		int term_id = termIDs.next();
    		LinkedHashMap<Integer,Integer> docID_tf_map = termPostingsMap.get(term_id);
    		int df = docID_tf_map.size();
    		double termWeightSum = 0.0d;
    		
    		dostfidf.writeBytes(term_id+"=");
    		dostfidf.flush();

    		dosVocab.writeBytes(term_id+"=");
    		dosVocab.flush();

    		dosbm25.writeBytes(term_id+"=");
    		dosbm25.flush();
    		
    		Iterator<Integer> termDocIdsIterator = docID_tf_map.keySet().iterator();
    		while(termDocIdsIterator.hasNext()){
    			int docID = termDocIdsIterator.next();
    			int tf = docID_tf_map.get(docID);
    			double k = 1;

    			String str = "["+docID+":"+tf+"],";
            	dosVocab.writeBytes(str);
            	dosVocab.flush();

            	double tf_idf = ( (1+  (Utility.log(tf, 2))) *   (Utility.log( ((double) N/ (double)df), 2)) );
    			termWeightSum += tf_idf;
    			dostfidf.writeBytes("["+docID+":"+tf_idf+"],");
    			dostfidf.flush();
    			
    			double bm25Weight = ((((double)k+1)*tf) / (tf+(double)k))* Utility.log( ((double)N+1)/(double)df, 2 );
    			dosbm25.writeBytes("["+docID+":"+bm25Weight+"],");
    			dosbm25.flush();
    			
    		}//end while
    		dostfidf.writeBytes("\n");
    		dostfidf.flush();
    		dosVocab.writeBytes("\n");
        	dosVocab.flush();
        	dosbm25.writeBytes("\n");
    		dosbm25.flush();
    		sbWeightSum.append(term_id+"="+termWeightSum+"\n");
    	}// end while

    	dostfidf.close();
    	dosVocab.close();
    	dosbm25.close();
    	
    	return sbWeightSum;
    }

    
    public static String getKeyValue(Object ob1, Object ob2){
    	return (ob1.toString()+DELIMETER+ob2.toString());
    }
    
    
    
    public static void insertionsort(Double[] termsArray,int lo, int hi,Integer[] A){ 
    	for(int j=2;j<termsArray.length;j++){
    		double key = termsArray[j];
    		int k2 = A[j];
    		int i= j-1;
    		while(i>=0 && termsArray[i]<key){
    			termsArray[i+1] = termsArray[i];
    			A[i+1] = A[i];
    			i--;
    		}
    		termsArray[i+1] = key;
    		A[i+1] = k2;
    	}
    }
    
    public static void insertionsort(Integer[] termsArray,int lo, int hi,Integer[] A){ 
    	for(int j=2;j<termsArray.length;j++){
    		int key = termsArray[j];
    		int k2 = A[j];
    		int i= j-1;
    		while(i>=0 && termsArray[i]<key){
    			termsArray[i+1] = termsArray[i];
    			A[i+1] = A[i];
    			i--;
    		}
    		termsArray[i+1] = key;
    		A[i+1] = k2;
    	}
    }
    
    public static void quicksort(Integer[] A, int lo, int hi,Double[] termsArray){
        if (lo < hi){
          int p = partition(A, lo, hi,termsArray);
          quicksort(A, lo, p - 1,termsArray);
          quicksort(A, p + 1, hi,termsArray);
        }
      }


      public static int partition(Integer[] A, int lo, int hi,Double[] termsArray){
          int pivot = A[hi];
          int i = lo; //place for swapping
          for (int j = lo; j <=(hi - 1);j++){
              if (A[j] <= pivot){
                  int temp = A[i];
                  A[i] = A[j];
                  A[j] = temp;
                  
                  /*swap double array*/
                  Double tempStr = termsArray[i];
                  termsArray[i] = termsArray[j];
                  termsArray[j] = tempStr;
                  
                  i = i + 1;
               }
          }
          int temp = A[i];
          A[i] = A[hi];
          A[hi] = temp;
          /*swap double*/
          Double tempStr = termsArray[i];
          termsArray[i] = termsArray[hi];
          termsArray[hi] = tempStr;
          return i;
    }
}
