/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.irs.utils;

/**
 *
 * @author Administrator
 */
public class JTime {

    public static final int SECONDS = 0;
    public static final int MINTUES = 1;
    public static final int MINTUES_AND_SECONDS = 2;
    public static final int HOURS = 3;
    
    private long startTime;
    
    public void startTime(){
        startTime = System.currentTimeMillis();
    }
    
    /**
     *
     * @param timeMode
     * @return
     */
    public String interval(int timeMode){
        long difference = System.currentTimeMillis()-startTime;
        if(timeMode == SECONDS){
            return ""+(difference/1000);
        }
        else if(timeMode == MINTUES){
            return ""+(difference/1000/60);
        }
        else if(timeMode == HOURS){
            return ""+(difference/1000/60/60);
        }
        else if(timeMode == MINTUES_AND_SECONDS){
            double d = (difference/1000);
            int seconds= (int) (d%60);
            int mintues= (int) (d/60);
            return mintues+" Mintues "+seconds+" Seconds";
        }
        return "";
    }
    
    public String intervalBreak(int timeMode){
        String srr = interval(timeMode);
        startTime();
        return srr;
    }
    
}
