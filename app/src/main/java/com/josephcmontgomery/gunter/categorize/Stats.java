package com.josephcmontgomery.gunter.categorize;


public class Stats {
	private int[] chunkStats = new int[10];
	
	public Stats(int indexToIncrease){
		increment(indexToIncrease);
	}
	
	public int getMostProbableChunkIndex(int numChunks){
		int maxIndex = 0;
		int maxProbability = chunkStats[maxIndex];
		for(int i = 0; i < numChunks && i < chunkStats.length; i++){
			if(chunkStats[i] > maxProbability){
				maxProbability = chunkStats[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	public void increment(int index){
		if(index < chunkStats.length){
			chunkStats[index]++;
		}
	}
}
