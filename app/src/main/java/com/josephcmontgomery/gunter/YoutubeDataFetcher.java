package com.josephcmontgomery.gunter;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;

import java.io.IOException;
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
        channelData = new ArrayList<YoutubeData>();
        for(int i = 0; i < channelIds.size(); i++){
            new RetrieveYoutubeChannelTask().execute(channelIds.get(i));
        }
        while(channelData.size() < channelIds.size());
        return channelData;
    }

    private YoutubeData getChannelTitleAndVideos(String channelId)throws Exception{
            YoutubeData channelData = new YoutubeData();
            Channel channel = getChannel(channelId);
            channelData.channelTitle = getChannelTitle(channel);
            channelData.videoTitles = getVideoTitles(getUploadsPlaylist(getUploadsId(channel)));
            return channelData;
    }

    private String getChannelTitle(Channel channel){
        return channel.getSnippet().getTitle();
    }

    private String getUploadsId(Channel channel){
        return channel.getContentDetails().getRelatedPlaylists().getUploads();
    }

    private Channel getChannel(String channelId) throws Exception{
        YouTube.Channels.List request = youtube.channels().list("snippet,contentDetails");
        request.setId(channelId);
        request.setKey(DeveloperKey.DEVELOPER_KEY);
        request.setFields("items(snippet/title,contentDetails/relatedPlaylists/uploads)");
        ChannelListResponse response = request.execute();
        return response.getItems().get(0);
    }

    private List<PlaylistItem> getUploadsPlaylist(String uploadsId) throws Exception{
        YouTube.PlaylistItems.List playlistRequest = youtube.playlistItems().list("snippet");
        playlistRequest.setPlaylistId(uploadsId);
        playlistRequest.setKey(DeveloperKey.DEVELOPER_KEY);
        PlaylistItemListResponse playlistResponse = playlistRequest.execute();
        return playlistResponse.getItems();
    }

    private String getVideoTitle(PlaylistItem videoItem){
        return videoItem.getSnippet().getTitle();
    }

    private String getUploadDate(PlaylistItem videoItem){ return null;}

    private ArrayList<String> getVideoTitles(List<PlaylistItem> playlistItems){
        ArrayList<String> videoTitles = new ArrayList<String>();
        Iterator<PlaylistItem> itemItr = playlistItems.iterator();
        while(itemItr.hasNext()){
            PlaylistItem item = itemItr.next();
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
                channelData.add(getChannelTitleAndVideos(channelId[0]));
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
            return null;
        }
    }
}
