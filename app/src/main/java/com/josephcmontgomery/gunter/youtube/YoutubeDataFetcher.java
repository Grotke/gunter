package com.josephcmontgomery.gunter.youtube;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Subscription;
import com.google.api.services.youtube.model.SubscriptionListResponse;
import com.josephcmontgomery.gunter.Auth;
import com.josephcmontgomery.gunter.MainActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Joseph on 7/29/2015.
 */
public class YoutubeDataFetcher {
    private static YouTube youtube;
    private static ArrayList<YoutubeData> channelData;
    private static GoogleAccountCredential credential;

    public static ArrayList<String> getSubscriptionsFromUserAccount(MainActivity activ){
        credential = GoogleAccountCredential.usingOAuth2(activ.getApplicationContext(), Arrays.asList(YouTubeScopes.YOUTUBE_READONLY));
        activ.setCredential(credential);
        //Must be run from main UI thread
        Intent intent = credential.newChooseAccountIntent();
        activ.startActivityForResult(intent, Auth.REQUEST_ACCOUNT_PICKER);
        return null;
    }

    public static void startThingy(MainActivity activ){
        new RetrieveUserDataTask().execute(activ);
    }
    public static void searchForReal(String searchTerm){
        new RetrieveYoutubeChannelTask().execute(searchTerm);
    }
    public static void search(String searchTerm){
        setupYoutube();
        try {
            ArrayList<String> strings = new ArrayList<String>();
            YouTube.Search.List search = youtube.search().list("snippet");
            search.setKey(DeveloperKey.DEVELOPER_KEY);
            search.setQ(searchTerm);
            search.setMaxResults(5L);
            search.setType("channel,items(snippet/title,snippet/channelId)");
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> results = searchResponse.getItems();
            for (SearchResult result : results) {
                Log.e("TITLE", result.getSnippet().getTitle());
                Log.e("ID", result.getSnippet().getChannelId());
                strings.add(result.getSnippet().getChannelId());
            }
            String bigID = TextUtils.join(",", strings);
            YouTube.Channels.List channelSearch = youtube.channels().list("statistics");
            channelSearch.setKey(DeveloperKey.DEVELOPER_KEY);
            channelSearch.setId(bigID);
            List<Channel> channelResponse = channelSearch.execute().getItems();
            for(Channel chan: channelResponse){
                Log.e("CHANNEL STATS", String.valueOf(chan.getStatistics().getSubscriberCount()));
            }
        }
        catch(Exception e){
            Log.e("ERROR", e.getMessage());
        }
    }

    public static void startThing(MainActivity activ){
        youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
                "gunter").build();
        try{
            YouTube.Subscriptions.List subRequest = youtube.subscriptions().list("snippet");
            subRequest.setMine(true);
            subRequest.setMaxResults(50L);
            List<Subscription> subs = new ArrayList<Subscription>();
            String nextToken = "";

            do {
                subRequest.setPageToken(nextToken);
                SubscriptionListResponse subListResponse = subRequest.execute();

                subs.addAll(subListResponse.getItems());

                nextToken = subListResponse.getNextPageToken();
            } while (nextToken != null);
            for(Subscription sub: subs){
                System.out.println("Got " + sub.getSnippet().getTitle());
                System.out.println();
            }
        } catch(UserRecoverableAuthIOException e){
            activ.startActivityForResult(e.getIntent(), Auth.REQUEST_AUTHORIZATION);
        }
        catch (GoogleJsonResponseException e) {
            System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                + e.getDetails().getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            System.err.println("Throwable: " + t.getMessage());
            t.printStackTrace();
        }
    }

    public static ArrayList<YoutubeData> getChannelData(ArrayList<String> channelIds){
        setupYoutube();
        channelData = new ArrayList<YoutubeData>();
        for(int i = 0; i < channelIds.size(); i++){
            new RetrieveYoutubeChannelTask().execute(channelIds.get(i));
        }
        while(channelData.size() < channelIds.size());
        return channelData;
    }

    private static YoutubeData getChannelTitleAndVideos(String channelId)throws Exception{
            YoutubeData channelData = new YoutubeData();
            Channel channel = getChannel(channelId);
            channelData.channelTitle = getChannelTitle(channel);
            channelData.videoTitles = getVideoTitles(getSearchResults(channelId));
            return channelData;
    }

    private static String getChannelTitle(Channel channel){
        return channel.getSnippet().getTitle();
    }

    private static Channel getChannel(String channelId) throws Exception{
        YouTube.Channels.List request = youtube.channels().list("snippet");
        request.setId(channelId);
        request.setKey(DeveloperKey.DEVELOPER_KEY);
        request.setFields("items(snippet/title)");
        ChannelListResponse response = request.execute();
        return response.getItems().get(0);
    }

    private static YouTube.Search.List setUpSearchRequest(String channelId) throws Exception{
        long resultsPerPage = 50;
        YouTube.Search.List searchRequest = youtube.search().list("snippet");
        searchRequest.setChannelId(channelId);
        searchRequest.setOrder("date");
        searchRequest.setType("video");
        searchRequest.setMaxResults(resultsPerPage);
        searchRequest.setFields("items(snippet/title)");
        searchRequest.setKey(DeveloperKey.DEVELOPER_KEY);
        return searchRequest;
    }

    private static List<SearchResult> getSearchResults(String channelId) throws Exception{
        YouTube.Search.List searchRequest = setUpSearchRequest(channelId);
        List<SearchResult> searchResults = new ArrayList<SearchResult>();
        SearchListResponse searchResponse = searchRequest.execute();
        searchResults.addAll(searchResponse.getItems());

        return searchResults;
    }

    private static String getVideoTitle(SearchResult videoItem){
        return videoItem.getSnippet().getTitle();
    }

    private static ArrayList<String> getVideoTitles(List<SearchResult> searchResults){
        ArrayList<String> videoTitles = new ArrayList<String>();
        Iterator<SearchResult> itemItr = searchResults.iterator();
        while(itemItr.hasNext()){
            SearchResult item = itemItr.next();
            videoTitles.add(getVideoTitle(item));
        }
        return videoTitles;
    }

    private static void setupYoutube(){
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

    private static class RetrieveYoutubeChannelTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... channelId) {
            try {
                //channelData.add(getChannelTitleAndVideos(channelId[0]));
                search(channelId[0]);
            } catch (Exception e) {
                if(e.getMessage() != null){
                    Log.e("error", e.getMessage());
                }
                else{
                    Log.e("Can't get Exception", "EXCEPTION");
                }
            }
            return null;
        }
    }

    public static GoogleAccountCredential getCredential(){
        return credential;
    }

    private static class RetrieveUserDataTask extends AsyncTask<MainActivity, Void, Void> {
        protected Void doInBackground(MainActivity... mainActivities) {
            try {
                startThing(mainActivities[0]);
            } catch (Exception e) {
                if(e.getMessage() != null){
                    Log.e("error", e.getMessage());
                }
                else{
                    Log.e("Can't get Exception", "EXCEPTION");
                }
            }
            return null;
        }
    }


}
