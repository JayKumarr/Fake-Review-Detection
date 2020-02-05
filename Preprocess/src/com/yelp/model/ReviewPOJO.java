package com.yelp.model;


import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mf15-10
 */
public class ReviewPOJO {
    private String review;
    private List<String> lemmetized;
    private Integer ID;
    
    public ReviewPOJO(String review, List<String> lemmetized) {
        this.review = review;
        this.lemmetized = lemmetized;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    
    
    
    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public List<String> getLemmetized() {
        return lemmetized;
    }

    public void setLemmetized(List<String> lemmetized) {
        this.lemmetized = lemmetized;
    }
    
    
}
