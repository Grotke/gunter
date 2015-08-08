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
        //createChannelIds();
        createTestChannelIds();
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
        channelIds.add("UCqg2eLFNUu3QN3dttNeOWkw"); //iHasCupquake
        channelIds.add("UCVdtW2E4vwvf8yh4FY5us9A"); //SSoHPKC
        channelIds.add("UCN-Klifn9C7kINwpIA0uOHw"); //Galm
        channelIds.add("UCV3kayetaucpObzusJbk9ag"); //Noah Baker
        channelIds.add("UCq54nlcoX-0pLcN5RhxHyug"); //Seananners
    }

    private void createTestChannelIds(){
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
}
