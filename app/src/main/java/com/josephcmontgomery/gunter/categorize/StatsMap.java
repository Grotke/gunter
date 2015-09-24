package com.josephcmontgomery.gunter.categorize;


import java.util.HashMap;

public class StatsMap {
	private HashMap<String, Stats> chunkStatsMap;
	
	public StatsMap(){
		chunkStatsMap = new HashMap<String, Stats>();
	}
	
	public void addFoundChunkStat(String channel, int index){
		Stats chunkStats = chunkStatsMap.get(channel);
		if(chunkStats != null){
			chunkStats.increment(index);
		}
		else{
			chunkStatsMap.put(channel,new Stats(index));
		}
	}
	//Returns -1 when index not found.
	private int getProbableChunkIndex(String channel, int chunksInTitle){
		Stats chunkStats = chunkStatsMap.get(channel);
		if(chunkStats != null){
			return chunkStats.getMostProbableChunkIndex(chunksInTitle);
		}
		return 0;
	}
	
	//Returns null when chunk stats isn't found.
	public Chunk getProbableChunk(Title title){
		return title.getChunkByIndex(getProbableChunkIndex(title.channelName, title.chunks.size()));
	}
}
