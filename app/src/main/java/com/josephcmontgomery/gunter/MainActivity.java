package com.josephcmontgomery.gunter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import java.util.ArrayList;

//TODO Find way to only check videos that are recent as X.
public class MainActivity extends ActionBarActivity {
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private YoutubeDataFetcher fetcher;
    private ArrayList<String> channelIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetcher = new YoutubeDataFetcher();
        expListView = (ExpandableListView) findViewById(R.id.channel_list);
        createChannelIds();
        listAdapter = new ExpandableListAdapter(fetcher.getChannelData(channelIds));
        listAdapter.setInflater((LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        expListView.setAdapter(listAdapter);
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

    private void createChannelIds(){
        channelIds = new ArrayList<String>();
        channelIds.add("UCqg2eLFNUu3QN3dttNeOWkw");
        channelIds.add("UCVdtW2E4vwvf8yh4FY5us9A");
        channelIds.add("UCN-Klifn9C7kINwpIA0uOHw");
        channelIds.add("UCV3kayetaucpObzusJbk9ag");
        channelIds.add("UCq54nlcoX-0pLcN5RhxHyug");
    }
}
