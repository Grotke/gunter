package com.josephcmontgomery.gunter.categorize;


import java.io.Serializable;

public class Chunk implements Serializable {
	public String chunk;
	public int chunkPos;
	
	public Chunk(String chunk, int chunkPos){
		this.chunk = chunk;
		this.chunkPos = chunkPos;
	}
	
	public String toString(){
		return chunk + " " + chunkPos;
	}
}
