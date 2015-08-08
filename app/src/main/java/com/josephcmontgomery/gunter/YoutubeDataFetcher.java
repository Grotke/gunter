package com.josephcmontgomery.gunter;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Joseph on 7/29/2015.
 */
public class YoutubeDataFetcher {
    private YouTube youtube;
    private ArrayList<YoutubeData> channelData;

    public YoutubeDataFetcher(){
        setupYoutube();
    }

    public ArrayList<YoutubeData> getChannelData(ArrayList<String> channelIds){
        Log.e("START", "Started process");
        channelData = new ArrayList<YoutubeData>();
        for(int i = 0; i < channelIds.size(); i++){
            new RetrieveYoutubeChannelTask().execute(channelIds.get(i));
        }
        while(channelData.size() < channelIds.size());
        Log.e("END", "Ended process");
        return channelData;
    }

    private YoutubeData getChannelTitleAndVideos(String channelId)throws Exception{
            YoutubeData channelData = new YoutubeData();
            Channel channel = getChannel(channelId);
            channelData.channelTitle = getChannelTitle(channel);
            channelData.videoTitles = getVideoTitles(getSearchResults(channelId));
            if(channelData.videoTitles.isEmpty()){
                channelData.videoTitles.add("No Recent Videos Found.");
            }
            return channelData;
    }

    private String getChannelTitle(Channel channel){
        return channel.getSnippet().getTitle();
    }

    private Channel getChannel(String channelId) throws Exception{
        YouTube.Channels.List request = youtube.channels().list("snippet");
        request.setId(channelId);
        request.setKey(DeveloperKey.DEVELOPER_KEY);
        request.setFields("items(snippet/title)");
        ChannelListResponse response = request.execute();
        return response.getItems().get(0);
    }

    private YouTube.Search.List setUpSearchRequest(String channelId) throws Exception{
        long resultsPerPage = 30;
        YouTube.Search.List searchRequest = youtube.search().list("snippet");
        searchRequest.setChannelId(channelId);
        searchRequest.setOrder("date");
        searchRequest.setType("video");
        searchRequest.setMaxResults(resultsPerPage);
        searchRequest.setPublishedAfter(TimeKeeper.getOldestAllowedVideoDate(new DateTime(System.currentTimeMillis())));
        searchRequest.setFields("items(snippet/title), nextPageToken");
        searchRequest.setKey(DeveloperKey.DEVELOPER_KEY);
        return searchRequest;
    }

    private YouTube.Search.List setUpTestDataSearchRequest(String channelId) throws Exception{
        long resultsPerPage = 50;
        YouTube.Search.List searchRequest = youtube.search().list("snippet");
        searchRequest.setChannelId(channelId);
        searchRequest.setOrder("date");
        searchRequest.setType("video");
        searchRequest.setMaxResults(resultsPerPage);
        searchRequest.setFields("items(snippet/title), nextPageToken");
        searchRequest.setKey(DeveloperKey.DEVELOPER_KEY);
        return searchRequest;
    }

    private List<SearchResult> getSearchResults(String channelId) throws Exception{
        //YouTube.Search.List searchRequest = setUpSearchRequest(channelId);
        YouTube.Search.List searchRequest = setUpTestDataSearchRequest(channelId);
        String nextToken = "";
        List<SearchResult> searchResults = new ArrayList<SearchResult>();
        //do {
            //searchRequest.setPageToken(nextToken);
            SearchListResponse searchResponse = searchRequest.execute();
            searchResults.addAll(searchResponse.getItems());
            //nextToken = searchResponse.getNextPageToken();
        //} while (nextToken != null);

        return searchResults;
    }

    private String getVideoTitle(SearchResult videoItem){
        return videoItem.getSnippet().getTitle();
    }

    private ArrayList<String> getVideoTitles(List<SearchResult> searchResults){
        ArrayList<String> videoTitles = new ArrayList<String>();
        Iterator<SearchResult> itemItr = searchResults.iterator();
        while(itemItr.hasNext()){
            SearchResult item = itemItr.next();
            videoTitles.add(getVideoTitle(item));
        }
        return videoTitles;
    }

    private void setupYoutube(){
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        HttpRequestInitializer initial  = new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
            }
        };
        youtube = new YouTube.Builder(transport, jsonFactory, initial)
                .setApplicationName("gunter").build();
    }

    private class RetrieveYoutubeChannelTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... channelId) {
            try {
                YoutubeData data = getChannelTitleAndVideos(channelId[0]);
                channelData.add(data);
                writeTitlesToFile(data);
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
            return null;
        }
    }

    private void writeTitlesToFile(YoutubeData data){
        File file = setupFile(getFilePath(), data.channelTitle+".txt");
        file.getParentFile().mkdirs();
        PrintWriter writer;
        try {
            writer = new PrintWriter(new FileOutputStream(file));
        }
        catch(Exception e){
            Log.e("FILE ERROR", e.getMessage());
            return;
        }

        for(int i =0; i < data.videoTitles.size(); i++){
            writer.println(data.videoTitles.get(i));
        }
        writer.flush();
        writer.close();
    }

    private File getFilePath(){
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

    private File setupFile(File basePath, String name){
        return new File(basePath,name);
    }
}
