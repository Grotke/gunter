package com.josephcmontgomery.gunter.categorize;


import java.io.Serializable;
import java.util.ArrayList;

public class Title implements Serializable{
	public String channelName;
	public String title;
	public Chunk definingChunk;
	public ArrayList<Chunk> chunks;
	
	public Title(String channelName,String title){
		definingChunk = null;
		this.channelName = channelName;
		this.title = title;
		chunks = new ArrayList<Chunk>();
	}
	
	public void addChunk(Chunk chunk){
		chunks.add(chunk);
	}
	
	//Returns null when index not in range.
	public Chunk getChunkByIndex(int index){
		if(index <= chunks.size()){
			return chunks.get(index);
		}
		return null;
	}
}
