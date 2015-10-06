package com.josephcmontgomery.gunter.display;

import com.josephcmontgomery.gunter.categorize.Series;

import java.util.ArrayList;

/**
 * Created by Joseph on 9/12/2015.
 */
public class DisplayData {
    public String channelTitle;
    public ArrayList<Series> seriesList;

    public DisplayData(String channelTitle, ArrayList<Series> series){
        this.channelTitle = channelTitle;
        seriesList = series;
    }
}
