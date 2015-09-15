package com.josephcmontgomery.gunter.categorize;


import java.io.Serializable;
import java.util.ArrayList;

public class Series implements Serializable{
	public String channelName;
	public String seriesName;
	public ArrayList<Title> titles;
	
	public Series(String channelName, String seriesName, ArrayList<Title> titles){
		this.channelName = channelName;
		this.seriesName = seriesName;
		this.titles = titles;
	}
}
