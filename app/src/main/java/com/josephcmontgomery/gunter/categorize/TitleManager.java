package com.josephcmontgomery.gunter.categorize;


import java.util.HashMap;

public class TitleManager {
	HashMap<String, ChunkManager> channelMap;

	public TitleManager(){
		channelMap = new HashMap<String, ChunkManager>();
	}
	
	public void registerChunk(String channel, String chunk, Title title){
		ChunkManager chunkMap = channelMap.get(channel);
		if(chunkMap == null){
			channelMap.put(channel, new ChunkManager(chunk, title));
		}
		else{
			chunkMap.registerChunk(chunk, title);
		}
	}
	
	//Returns null when ChunkManager isn't found.
	public ChunkManager getChunkManager(String channelName){
		return channelMap.get(channelName);
	}
}
