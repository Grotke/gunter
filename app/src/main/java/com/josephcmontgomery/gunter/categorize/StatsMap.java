package com.josephcmontgomery.gunter.categorize;


import android.util.Log;

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
		Log.e("CHUNK INDEX", "Probable Chunk wasn't found");
		return 0;
	}
	
	//Returns null when chunk stats isn't found.
	public Chunk getProbableChunk(Title title){
		int index = getProbableChunkIndex(title.channelName, title.chunks.size());
		if(index != -1){
			return title.getChunkByIndex(index);
		}
		Log.e("CHUNK", "Probable chunk wasn't found");
		return null;
	}
}
