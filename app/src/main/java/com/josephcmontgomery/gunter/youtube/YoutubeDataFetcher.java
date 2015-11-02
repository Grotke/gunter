package com.josephcmontgomery.gunter.youtube;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
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
import com.google.common.collect.Lists;
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
    private static ArrayList<GoogleAccountCredential> credentials = new ArrayList<GoogleAccountCredential>();

    public YoutubeDataFetcher(){
        //setupYoutube();
    }

    public static ArrayList<String> getSubscriptionsFromUserAccount(MainActivity activ){
        final int REQUEST_ACCOUNT_PICKER = 1;
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.readonly");
       // try {
            // Authorize the request.
            //Credential credential = Auth.authorize(scopes, "readsubscriptions");
            /*GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(activ.getApplicationContext(), Arrays.asList(YouTubeScopes.YOUTUBE_READONLY));
            //SharedPreferences settings = MainActivity.getPreferences(Context.MODE_PRIVATE);
            activ.setCredential(credential);
            activ.startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);*/
            credential = GoogleAccountCredential.usingOAuth2(activ.getApplicationContext(), Arrays.asList(YouTubeScopes.YOUTUBE_READONLY));
            //SharedPreferences settings = MainActivity.getPreferences(Context.MODE_PRIVATE);
            activ.setCredential(credential);
            activ.setCredentialList(credentials);
            Intent intent = credential.newChooseAccountIntent();
            //Log.e("INTENT NAME", intent.getComponent().getClassName());
            activ.startActivityForResult(intent, REQUEST_ACCOUNT_PICKER);
            //new RetrieveUserDataTask().execute(activ);
            /*while(credentials.size() < 1){
                //Log.e("STATUS", "not done");
            }*/
            Log.e("DONE", "Done with credential " + credential.getSelectedAccountName());

// YouTube client
           /* service =
                    new com.google.api.services.youtube.YouTube.Builder(transport, jsonFactory, credential)
                            .setApplicationName("Google-YouTubeAndroidSample/1.0").build();*/

            // This object is used to make YouTube Data API requests.
            /*Log.e("FIRST", "Got Here");
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
                    "gunter").build();

            YouTube.Subscriptions.List subRequest = youtube.subscriptions().list("snippet");
            subRequest.setMine(true);
            List<Subscription> subs = subRequest.execute().getItems();
            for(Subscription sub: subs){
                System.out.println("Got " + sub.getSnippet().getChannelTitle());
                System.out.println();
            }
            Log.e("SECOND", "Got Here");
        } catch (GoogleJsonResponseException e) {
            System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
            e.printStackTrace();

        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            System.err.println("Throwable: " + t.getMessage());
            t.printStackTrace();
        }*/
        return null;
    }

    public static void startThingy(MainActivity activ){
        new RetrieveUserDataTask().execute(activ);
    }

    public static void startThing(MainActivity activ){
        Log.e("FIRST", "Got Here");
        youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
                "gunter").build();
        try{
            Log.e("IN DA BLOCk", "RIGHT HERE");
            Log.e("TOKEN", credential.getToken());
        YouTube.Subscriptions.List subRequest = youtube.subscriptions().list("snippet");
        subRequest.setMine(true);
            Log.e("TOKEN", credential.getToken());
            //subRequest.setOauthToken(credential.getToken());
            //subRequest.setOauthToken(credential.getToken());
            Log.e("EXECUTE", "HERE");
            subRequest.setMaxResults(20L);
        List<Subscription> subs = subRequest.execute().getItems();
            Log.e("NOT YET", "NOPE");
        for(Subscription sub: subs){
            System.out.println("Got " + sub.getSnippet().getTitle());
            System.out.println();
        }
        Log.e("SECOND", "Got Here");
    } catch(UserRecoverableAuthException e){
            activ.startActivityForResult(e.getIntent(), 2);
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
                channelData.add(getChannelTitleAndVideos(channelId[0]));
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
        final int REQUEST_ACCOUNT_PICKER = 1;
        private GoogleAccountCredential credential;
        @Override
        protected void onPreExecute(){
            credential = YoutubeDataFetcher.getCredential();
        }

        protected Void doInBackground(MainActivity... mainActivities) {
            try {
                /*MainActivity activ = mainActivities[0];
                //credential = GoogleAccountCredential.usingOAuth2(activ.getApplicationContext(), Arrays.asList(YouTubeScopes.YOUTUBE_READONLY));
                //SharedPreferences settings = MainActivity.getPreferences(Context.MODE_PRIVATE);
                //activ.setCredential(credential);
                Intent intent = credential.newChooseAccountIntent();
                Log.e("INTENT NAME", intent.getComponent().getClassName());
                //activ.startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
                Log.e("START", "Started activity result");*/
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
