/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yelp.model;

import java.util.Date;

/**
 *
 * @author Administrator
 */
public class Review {
    String reviewContent;
    Date postingDate;
    int rating;
    int funnyCount;
    int coolCount;
    int usefulCount;
    String reviewerID;
    String restaurantID;
    String reviewID;

    public Review() {
    }

    public Review(int rating, String reviewID) {
        this.rating = rating;
        this.reviewID = reviewID;
    }

    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }

    
    
    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public Date getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Date postingDate) {
        this.postingDate = postingDate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getFunnyCount() {
        return funnyCount;
    }

    public void setFunnyCount(int funnyCount) {
        this.funnyCount = funnyCount;
    }

    public int getCoolCount() {
        return coolCount;
    }

    public void setCoolCount(int coolCount) {
        this.coolCount = coolCount;
    }

    public int getUsefulCount() {
        return usefulCount;
    }

    public void setUsefulCount(int usefulCount) {
        this.usefulCount = usefulCount;
    }

    public String getReviewerID() {
        return reviewerID;
    }

    public void setReviewerID(String reviewerID) {
        this.reviewerID = reviewerID;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }
    
    
}
