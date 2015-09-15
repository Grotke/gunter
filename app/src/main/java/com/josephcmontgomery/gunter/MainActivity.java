package com.josephcmontgomery.gunter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.commonsware.cwac.merge.MergeAdapter;
import com.josephcmontgomery.gunter.categorize.Categorize;
import com.josephcmontgomery.gunter.categorize.Series;

import java.util.ArrayList;

//TODO Find way to only check videos that are recent as X.
public class MainActivity extends ActionBarActivity {
    private MyListAdapter listAdapter;
    //private ChildExpandableListAdapter listAdapter;
    private ListView listView;
    private YoutubeDataFetcher fetcher;
    private ArrayList<String> channelIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MergeAdapter thisAdapter = new MergeAdapter();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("EARLY CREATE", "All good here");
        /*fetcher = new YoutubeDataFetcher();
        expListView = (ExpandableListView) findViewById(R.id.channel_list);
        createChannelIds();
        listAdapter = new ChildExpandableListAdapter(fetcher.getChannelData(channelIds));
        listAdapter.setInflater((LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        expListView.setAdapter(listAdapter);*/
        fetcher = new YoutubeDataFetcher();
        listView = (ListView) findViewById(R.id.main_list);
        createNewChannelIds();
        Log.d("MID CREATE", "All good here");
        listAdapter = new MyListAdapter(processYoutubeData(fetcher.getChannelData(channelIds)));
        //listAdapter = new ChildExpandableListAdapter(processYoutubeData(fetcher.getChannelData(channelIds)).get(0).seriesList);
        Log.d("LATE MID CREATE", "All good here");
        listAdapter.setInflater((LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        thisAdapter.addAdapter(listAdapter);
        listView.setAdapter(thisAdapter);
        Log.d("LATE CREATE", "All good here");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {

                Intent intent = new Intent(MainActivity.this, SeriesDisplayActivity.class);
                intent.putExtra("channel_title", ((DisplayData)a.getItemAtPosition(position)).channelTitle);
                intent.putExtra("series", ((DisplayData) a.getItemAtPosition(position)).seriesList);

                startActivity(intent);
            }
        });
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

    private void createNewChannelIds(){
        channelIds = new ArrayList<String>();
        channelIds.add("UC0M0rxSz3IF0CsSour1iWmw"); //AVGN
        channelIds.add("UCCbfB3cQtkEAiKfdRQnfQvw"); //Jesse Cox
        channelIds.add("UCy1Ms_5qBTawC-k7PVjHXKQ"); //TotalBiscuit
        channelIds.add("UCq54nlcoX-0pLcN5RhxHyug"); //Seananners
        channelIds.add("UCN-Klifn9C7kINwpIA0uOHw"); //Galm
        channelIds.add("UCUUUpaMp8DV6KUOfQwoIiLg"); //WOWPRESENTS
        channelIds.add("UC-lHJZR3Gqxm24_Vd_AJ5Yw"); //PewDiePie
        channelIds.add("UCqg2eLFNUu3QN3dttNeOWkw"); //iHasCupquake
        channelIds.add("UCcMTZY1rFXO3Rj44D5VMyiw"); //Machinima
        channelIds.add("UCHcOgmlVc0Ua5RI4pGoNB0w"); //Pungence
        channelIds.add("UCJTWU5K7kl9EE109HBeoldA"); //Generikb
        channelIds.add("UClu2e7S8atp6tG2galK9hgg"); //BdoubleO
        channelIds.add("UCy4earvTTlP5OUpNRvPI7TQ"); //wowcrendor
        channelIds.add("UCvu83617k-_4ycNFdLyRz6A"); //thewolvesatmydoor
        channelIds.add("UCK376qNDlNZZDNHsnaWuTeg"); //AntVenom
        channelIds.add("UCyZA5Ysa33gA89sCdWJQojQ"); //Caveman
        channelIds.add("UCURh19hEVawK-H0Wl7KnR5Q"); //OhmWrecker
        channelIds.add("UCOHBVUV8aDg4tQiHnUqi_QA"); //Mathas
    }

    private ArrayList<DisplayData> processYoutubeData(ArrayList<YoutubeData> data){
        ArrayList<DisplayData> dispData = new ArrayList<DisplayData>();
        for(YoutubeData yData: data){
            /*ChildExpandableListAdapter adapt = new ChildExpandableListAdapter(Categorize.categorize(yData.channelTitle, yData.videoTitles));
            adapt.setInflater((LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE));*/
            ArrayList<Series> series = Categorize.categorize(yData.channelTitle, yData.videoTitles);
            for(Series s: series){
                Log.e(s.channelName, " has series " + s.seriesName);
            }
            DisplayData newData = new DisplayData(yData.channelTitle, series);
            Log.e(newData.channelTitle, " has " + newData.seriesList.size() + " series.");
            dispData.add(newData);
        }
        Log.e("DISPLAY STATE", "Processing all finished");
        return dispData;
    }
}
