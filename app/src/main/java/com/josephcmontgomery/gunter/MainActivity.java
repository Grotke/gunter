package com.josephcmontgomery.gunter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

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

//TODO Find way to only check videos that are recent as X.
public class MainActivity extends ActionBarActivity {
    private static YouTube youtube;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private ArrayList<String> parents;
    private ArrayList<ArrayList<String>> children;
    private YoutubeDataFetcher fetcher;
    private ArrayList<String> channelIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetcher = new YoutubeDataFetcher();
        //setupYoutube();
        expListView = (ExpandableListView) findViewById(R.id.channel_list);
        //parents = new ArrayList<String>();
        //children = new ArrayList<ArrayList<String>>();
        //listAdapter = new ExpandableListAdapter(parents,children);
        createChannelIds();
        listAdapter = new ExpandableListAdapter(fetcher.getChannelData(channelIds));
        listAdapter.setInflater((LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        expListView.setAdapter(listAdapter);
        /*for(int i = 0; i < channelIds.length; i++) {
            new RetrieveFeedTask(channelIds[i], i).execute();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getYoutubeChannelName(String channelId, int index){
        try {
            YouTube.Channels.List request = youtube.channels().list("snippet,contentDetails");
            request.setId(channelId);
            request.setKey(DeveloperKey.DEVELOPER_KEY);
            request.setFields("items(snippet/title,contentDetails/relatedPlaylists/uploads)");
            ChannelListResponse response = request.execute();
            List<Channel> channels = response.getItems();
            parents.add(index,channels.get(0).getSnippet().getTitle());
            String uploadsId = channels.get(0).getContentDetails().getRelatedPlaylists().getUploads();

            YouTube.PlaylistItems.List playlistRequest = youtube.playlistItems().list("snippet");
            playlistRequest.setPlaylistId(uploadsId);
            playlistRequest.setKey(DeveloperKey.DEVELOPER_KEY);
            PlaylistItemListResponse playlistResponse = playlistRequest.execute();
            List<PlaylistItem> playlistItems = playlistResponse.getItems();
            ArrayList<String> titles = new ArrayList<String>();
            Iterator<PlaylistItem> itemItr = playlistItems.iterator();
            while(itemItr.hasNext()){
                PlaylistItem item = itemItr.next();
                titles.add(item.getSnippet().getTitle());
            }
            children.add(index,titles);
        }
        catch (Exception e){
            Log.e("error",e.getMessage());
        }
    }

    private class RetrieveFeedTask extends AsyncTask<String, Integer, Void> {

        private Exception exception;
        private String channelId;
        private int position;

        public RetrieveFeedTask(String channelId, int position){
            this.channelId = channelId;
            this.position = position;
        }

        protected Void doInBackground(String... channelIds) {
            try {
                getYoutubeChannelName(channelId,position);
            } catch (Exception e) {
                this.exception = e;
            }
            return null;
        }

        protected void onPostExecute(Void i) {
            listAdapter.update(parents,children);
        }
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

    private void createChannelIds(){
        channelIds = new ArrayList<String>();
        channelIds.add("UCq54nlcoX-0pLcN5RhxHyug");
        channelIds.add("UCqg2eLFNUu3QN3dttNeOWkw");
    }
}
