package com.josephcmontgomery.gunter.categorize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CategorizationTools {
	public static Title packTitle(String channelName, String searchStr, TitleManager manage){
		ArrayList<MatchedPair> pairs = createMatchedPairs(searchStr);
		ArrayList<String> chunks = new ArrayList<String>();
		int strLength = searchStr.length();
		MatchedPair sectionToProcess = new MatchedPair(0, strLength);
		while(sectionToProcess.left < strLength){
			if(!pairs.isEmpty()){
				sectionToProcess = processAPossiblePair(sectionToProcess, pairs, chunks, searchStr);
			}
			else{
				sectionToProcess = processTextWithoutPair(sectionToProcess, chunks, searchStr, strLength);
			}
		}
		return createAndRegisterTitleWithChunks(channelName, searchStr, chunks, manage);
	}
	
	private static MatchedPair processAPossiblePair(MatchedPair sectionToProcess, ArrayList<MatchedPair> pairs, ArrayList<String> chunks, String searchStr){
		int pairLeft = pairs.get(0).left;
		if(textIsBeforeAPair(sectionToProcess.left, pairLeft)){
			return processTextWithoutPair(sectionToProcess, chunks, searchStr, pairLeft);
		}
		else if (pairIsNestedInAnotherPair(sectionToProcess.left, pairLeft)){
			pairs.remove(0);
		}
		else if (atAPair(sectionToProcess.left, pairLeft)){
			return processAPair(pairs.get(0), chunks, searchStr);
		}
		
		return sectionToProcess;
	}
	
	private static boolean textIsBeforeAPair(int currentIndex, int leftPairIndex){
		return currentIndex < leftPairIndex;
	}
	
	private static boolean pairIsNestedInAnotherPair(int currentIndex, int leftPairIndex){
		return currentIndex > leftPairIndex;
	}
	
	private static boolean atAPair(int currentIndex, int leftPairIndex){
		return currentIndex == leftPairIndex;
	}
	
	private static MatchedPair processTextWithoutPair(MatchedPair sectionToProcess, ArrayList<String> chunks, String searchStr, int newEndIndex){
		MatchedPair pairToReturn = new MatchedPair(sectionToProcess.left, newEndIndex);
		chunks.addAll(chunkify(searchStr.substring(pairToReturn.left, pairToReturn.right)));
		pairToReturn.left = pairToReturn.right;
		return pairToReturn;
	}
	
	private static MatchedPair processAPair(MatchedPair pairToProcess, ArrayList<String> chunks, String searchStr){
		//Drop the left bracket when processing.
		MatchedPair pairToReturn = new MatchedPair(pairToProcess.left+1, pairToProcess.right);
		chunks.add(standardizeTitle(searchStr.substring(pairToReturn.left, pairToReturn.right)));
		//Drop the right bracket when processing.
		pairToReturn.left = pairToReturn.right +1; 
		return pairToReturn;
	}
	
	private static Title createAndRegisterTitleWithChunks(String channelName, String fullTitle, ArrayList<String> chunks, TitleManager manage){
		Title title = new Title(channelName, fullTitle);
		int chunkCounter = 0;
		for(String chunk : chunks){
			if(chunkIsNotBlankOrWhitespace(chunk)){
				manage.registerChunk(channelName, chunk.trim(), title);
				title.addChunk(new Chunk(chunk.trim(),chunkCounter));
				chunkCounter++;
			}
		}
		return title;
	}
	
	private static boolean chunkIsNotBlankOrWhitespace(String chunk){
		return !chunk.matches("[\\W]+") && !chunk.matches("^$");
	}
	
	private static String standardizeTitle(String title){
		String[] genericWords = {"EP","PT","PART","EPISODE","HD","FINALE"};
		String upperCaseTitle = title.toUpperCase();
		//String numberlessTitle = lowerCaseTitle.replaceAll("[a-z]*[0-9]+[a-z]*","");
		String numberlessTitle = upperCaseTitle.replaceAll(standAloneNumbers(),"").replaceAll("[!#?.]+", "");
		String noGenericWordsTitle = numberlessTitle;
		for(String word : genericWords){
			//Finds stand alone generic words
			noGenericWordsTitle  = noGenericWordsTitle.replaceAll("\\b"+ word+"\\b", " ");
		}
		
		String noExcessWhitespace = noGenericWordsTitle.replaceAll("\\s+", " ");
		String noEmptyBrackets = noExcessWhitespace.replaceAll("[\\[\\(]{1}\\W*[\\]\\)]{1}", "");
		return noEmptyBrackets.trim();
	}
	
	//Finds numbers with word boundaries on each side.
	private static String standAloneNumbers(){
		return "\\b[0-9]+\\b";
	}
	
	//A delimiter that doesn't have a space or a delimiter to the right must be part of a word. 
	private static String delimitersWithSpaceToTheRight(){
		return "[:|-]+[\\s:|-]";
	}
	
	private static ArrayList<String> chunkify(String fragment){
		String[] chunksArray = standardizeTitle(fragment).split(delimitersWithSpaceToTheRight());
		ArrayList<String> chunkList = new ArrayList<String>();
		for(String chunk: chunksArray){
			chunkList.add(chunk.trim());
		}
		return chunkList;
	}
	
	private static ArrayList<MatchedPair> createMatchedPairs(String searchStr){
		ArrayList<MatchedPair> complete = new ArrayList<MatchedPair>();
		complete.addAll(findBrackets(searchStr, '[',']'));
		complete.addAll(findBrackets(searchStr, '(', ')'));
		Collections.sort(complete, new MatchedPairComparator());
		return complete;
	}
	
	private static ArrayList<MatchedPair> findBrackets(String searchStr, char left, char right){
		return findMatchedPairs(findCharIndicies(searchStr, left), findCharIndicies(searchStr, right));
	}
	
	//Runs through left and right bracket indicies and matches the last left bracket with the first right bracket
	//as long as the right bracket comes after the left bracket.
	private static ArrayList<MatchedPair> findMatchedPairs(ArrayList<Integer> leftBrackets, ArrayList<Integer> rightBrackets){
		ArrayList<MatchedPair> matchedPairs = new ArrayList<MatchedPair>();
		while(!leftBrackets.isEmpty() && !rightBrackets.isEmpty()){
			int i = leftBrackets.size()-1;
			boolean matchFound = false;
			while(i >= 0 && !matchFound){
				if(leftBrackets.get(i) > rightBrackets.get(0)){
					i--;
				}
				else{
					matchFound = true;
					matchedPairs.add(new MatchedPair(leftBrackets.remove(i), rightBrackets.remove(0)));
				}
			}
			//Stray right bracket
			if(!matchFound){
				rightBrackets.remove(0);
			}
		}
		return matchedPairs;
	}
	
	private static ArrayList<Integer> findCharIndicies(String searchStr, char charToFind){
		ArrayList<Integer> indicies = new ArrayList<Integer>();
		int index, searchStartIndex = 0;
		while((index = searchStr.indexOf(charToFind, searchStartIndex)) != -1){
			indicies.add(index);
			searchStartIndex = index + 1;
		}
		
		return indicies;
	}
	
	private static class MatchedPair {
		public int left;
		public int right;
		
		public MatchedPair(int leftIndex, int rightIndex){
			left = leftIndex;
			right = rightIndex;
		}
	}
	
	private static class MatchedPairComparator implements Comparator<MatchedPair>{
		public int compare(MatchedPair a, MatchedPair b){
			return a.left < b.left ? -1 : a.left > b.left ? 1: 0;
		}
	}
}
