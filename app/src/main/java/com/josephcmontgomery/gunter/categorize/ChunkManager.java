package com.josephcmontgomery.gunter.categorize;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChunkManager {
	private HashMap<String, ArrayList<Title>> chunkMap;
	
	public ChunkManager(){
		chunkMap = new HashMap<String, ArrayList<Title>>();
	}
	
	public ChunkManager(String chunk, Title title){
		chunkMap = new HashMap<String, ArrayList<Title>>();
		registerChunk(chunk, title);
	}
	
	public void registerChunk(String chunk, Title title){
		ArrayList<Title> titles = chunkMap.get(chunk);
		if(titles == null){
			ArrayList<Title> temp = new ArrayList<Title>();
			temp.add(title);
			chunkMap.put(chunk, temp);
		}
		else{
			titles.add(title);
		}
	}
	
	public Set<Map.Entry<String,ArrayList<Title>>> getEntrySet(){
		return chunkMap.entrySet();
	}
	
	//Returns -1 when no chunk is found.
	public int getNumTitlesWithChunk(String chunk){
		if(chunkMap.get(chunk) != null){
			return chunkMap.get(chunk).size();
		}
		return -1;
	}
}
