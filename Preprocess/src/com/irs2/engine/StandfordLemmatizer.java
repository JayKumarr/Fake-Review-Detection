/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.irs2.engine;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.ling.CoreLabel;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author Jay Kumarr
 */
public class StandfordLemmatizer {

    protected StanfordCoreNLP pipeline;
    private static Set<String> specialChars = new LinkedHashSet<>(Arrays.asList("\"", "!", ",", ".", "!!", "-", "!!!", "#", "$", "%", "&", "'", "+", "/", ";", "<", ">", "*", "@", "?", "|", "`", "``", "=--", "''", ".."));

    public StandfordLemmatizer() {
        // Create StanfordCoreNLP object properties, with POS tagging
        // (required for lemmatization), and lemmatization
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");

        // StanfordCoreNLP loads a lot of models, so you probably
        // only want to do this once per execution
        this.pipeline = new StanfordCoreNLP(props);
    }

    public List<String> tokenizer(String documentText) {
        List<String> lemmas = new LinkedList<String>();

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);

        // run all Annotators on this text
        this.pipeline.annotate(document);

        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                String lemma = token.originalText();
                if(specialChars.contains(lemma))continue;
                int index = lemma.indexOf("/");
                if( index > -1){
                    String first = lemma.substring(0,index);
                    String second = lemma.substring(index+1);
                    if(!first.trim().isEmpty())lemmas.add(first);
                    if(!second.trim().isEmpty())lemmas.add(second);
                    continue;
                }
                index = lemma.indexOf(".");
                if( index > -1){
                    String first = lemma.substring(0,index);
                    String second = lemma.substring(index+1);
                    if(!first.trim().isEmpty())lemmas.add(first);
                    if(!second.trim().isEmpty())lemmas.add(second);
                    continue;
                }
                if (!lemma.trim().isEmpty()) {
                    lemma = lemma.replaceAll(",", "");
                }
                lemmas.add(lemma);
            }
        }
        return lemmas;
    }

    public List<String> lemmatize(String documentText, boolean toLowerCase) {
        List<String> lemmas = new LinkedList<String>();

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);

        // run all Annotators on this text
        this.pipeline.annotate(document);

        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the list of lemmas
                String lemma = token.get(LemmaAnnotation.class);
                if (lemma.length() < 2 || specialChars.contains(lemma) || lemma.startsWith("!!") || lemma.startsWith("##") || lemma.equals("..")) {
                    continue;
                }
                int index = lemma.indexOf("/");
                if (index > -1) {
                    String first = lemma.substring(0, index);
                    String second = lemma.substring(index + 1);
                    if (!first.trim().isEmpty()) {
                        lemmas.add(first);
                    }
                    if (!second.trim().isEmpty()) {
                        lemmas.add(second);
                    }
                    continue;
                }
                index = lemma.indexOf(".");
                if (index > -1) {
                    String first = lemma.substring(0, index);
                    String second = lemma.substring(index + 1);
                    if (!first.trim().isEmpty()) {
                        lemmas.add(first);
                    }
                    if (!second.trim().isEmpty()) {
                        lemmas.add(second);
                    }
                    continue;
                }
                if (!lemma.trim().isEmpty()) {
                    lemma = lemma.replaceAll(",", "");
                }
                lemmas.add(lemma);
            }
        }

        return lemmas;
    }
}
