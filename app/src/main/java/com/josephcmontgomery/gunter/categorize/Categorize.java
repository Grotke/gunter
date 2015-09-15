package com.josephcmontgomery.gunter.categorize;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

public class Categorize {
	private static StatsMap stats = new StatsMap();
	private static TitleManager titleManager = new TitleManager();
	
	
	//TODO remove print statements before integrating.
	public static ArrayList<Series> categorize(String channelName, ArrayList<String> titles){
		ChunkManager chunkManage = new ChunkManager();
		ArrayList<Title> packedTitles = packTitles(channelName, titles);
		ArrayList<Series> packedSeries;
		
		ArrayList<Title> waitingList = new ArrayList<Title>();
		System.out.println();
		int titleCount = 1;
		for(Title title : packedTitles){
			Chunk topChunk = getDefiningChunk(titleManager.getChunkManager(channelName), title);
			if(topChunk == null){
				waitingList.add(title);
			}
			else{
				titleCount = processTitle(title, topChunk, titleCount, chunkManage);
			}
		}
		if(!waitingList.isEmpty()) {
			//Log.d("WAITING LIST", "Waiting List has " + waitingList.get(0).title);
			processUniqueTitles(waitingList, chunkManage, titleCount);
		}
		packedSeries = packSeries(channelName, chunkManage);
		
		return packedSeries;
	}
	
	private static ArrayList<Title> packTitles(String channelName, ArrayList<String> titles){
		ArrayList<Title> packedTitles = new ArrayList<Title>();
		for(String title: titles){
			packedTitles.add(CategorizationTools.packTitle(channelName, title, titleManager));
		}
		return packedTitles;
	}
	
	private static ArrayList<Series> packSeries(String channelName, ChunkManager chunkManage){
	    Iterator<Entry<String, ArrayList<Title>>> it = chunkManage.getEntrySet().iterator();
	    ArrayList<Series> packedSeries = new ArrayList<Series>();
	    while (it.hasNext()) {
	        Entry<String, ArrayList<Title>> pair = (Entry<String, ArrayList<Title>>)it.next();
	        packedSeries.add(new Series(channelName, (String)pair.getKey(), (ArrayList<Title>)pair.getValue()));
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	        
	    return packedSeries;
	}
	
	//TODO Remove print statements before integrating.
	private static int processTitle(Title title, Chunk topChunk, int titleCount, ChunkManager chunkManage){
		title.definingChunk = topChunk;
		chunkManage.registerChunk(topChunk.chunk, title);
		titleCount++;
		return titleCount;
	}
	
	private static void processUniqueTitles(ArrayList<Title> titles, ChunkManager chunkManage, int currentTitleCount){
		for(Title title: titles){
			Chunk topChunk = getProbableChunk(title);
			currentTitleCount = processTitle(title, topChunk, currentTitleCount, chunkManage);
		}
	}
	
	//Can return null if stats not found.
	private static Chunk getProbableChunk(Title title){
		return stats.getProbableChunk(title);
	}
	
	private static Chunk getDefiningChunk(ChunkManager chunkMap, Title title){
		ArrayList<Chunk> plausibleChunks = getLowestPlausibleChunks(chunkMap, title);
		//All chunks have only 1 title associated.
		if(plausibleChunks == null){
			return null;
		}
		Chunk currentTopChunk = plausibleChunks.get(0);
		for(Chunk chunk: plausibleChunks){
			if(chunkIsTwiceAsLongAsTopChunk(chunk, currentTopChunk)){
				currentTopChunk = chunk;
			}
		}
		stats.addFoundChunkStat(title.channelName, currentTopChunk.chunkPos);
		return currentTopChunk;
	}
	
	private static boolean chunkIsTwiceAsLongAsTopChunk(Chunk chunk, Chunk topChunk){
		return chunk.chunk.length() >= 2 * topChunk.chunk.length();
	}
	
	//Find lowest chunks that have more than 1 title associated.
	private static ArrayList<Chunk> getLowestPlausibleChunks(ChunkManager chunkMap,Title title){
		ArrayList<Chunk> lowChunks = new ArrayList<Chunk>();
		if(chunkMap == null){
			return lowChunks;
		}
		int currentLow = 100000;
		for(Chunk chunk: title.chunks){
			int chunkSize = chunkMap.getNumTitlesWithChunk(chunk.chunk);
			if(chunkSize > 1 && chunkSize <= currentLow){
				//New low found
				if(chunkSize < currentLow){
					currentLow = chunkSize;
					lowChunks.clear();
				}
				lowChunks.add(chunk);
			}
		}
		//All chunks have only 1 title associated.
		if(lowChunks.isEmpty()){
			return null;
		}
		return lowChunks;
	}
}
