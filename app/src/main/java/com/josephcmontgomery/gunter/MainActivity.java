package com.josephcmontgomery.gunter;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.commonsware.cwac.merge.MergeAdapter;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.josephcmontgomery.gunter.categorize.Categorize;
import com.josephcmontgomery.gunter.categorize.Series;
import com.josephcmontgomery.gunter.categorize.Title;
import com.josephcmontgomery.gunter.display.ChannelListAdapter;
import com.josephcmontgomery.gunter.display.DisplayData;
import com.josephcmontgomery.gunter.youtube.YoutubeData;
import com.josephcmontgomery.gunter.youtube.YoutubeDataFetcher;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity{
    private ListView listView;
    private ArrayList<String> channelIds;
    private ChannelListAdapter listAdapter;
    private GoogleAccountCredential credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //launchSubscriptionPull();
        launchVideoPull();
    }

    private void launchSubscriptionPull(){
        YoutubeDataFetcher.getSubscriptionsFromUserAccount(this);
    }

    private void launchVideoPull(){
        createNewChannelIds();
        setUpViewsAndAdapters(processYoutubeData(YoutubeDataFetcher.getChannelData(channelIds)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                //Get Account Name
                case Auth.REQUEST_ACCOUNT_PICKER:
                    if (data != null && data.getExtras() != null) {
                        String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                        if (accountName != null) {
                            credential.setSelectedAccountName(accountName);
                            YoutubeDataFetcher.getUserData(this);
                        }
                    }
                    break;
                //Authorize
                case Auth.REQUEST_AUTHORIZATION:
                    if (resultCode == Activity.RESULT_OK) {
                        // replay the same operations
                        YoutubeDataFetcher.getUserData(this);
                    }
                    break;
            }
        }
    }

    public void setCredential(GoogleAccountCredential cred){
        credential = cred;
    }

    private void setUpViewsAndAdapters(ArrayList<DisplayData> dataToView){
        MergeAdapter mergeAdapter = new MergeAdapter();
        listView = (ListView) findViewById(R.id.main_list);
        listAdapter = new ChannelListAdapter(dataToView);
        listAdapter.setInflater((LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        mergeAdapter.addAdapter(listAdapter);
        listView.setAdapter(mergeAdapter);
        setUpViewClickListener();
    }

    private void setUpViewClickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                Intent intent = new Intent(MainActivity.this, SeriesDisplayActivity.class);
                DisplayData data = (DisplayData) a.getItemAtPosition(position);
                intent.putExtra("channel_title", data.channelTitle);
                intent.putExtra("series", data.seriesList);
                startActivity(intent);
            }
        });
    }

    private ArrayList<DisplayData> processYoutubeData(ArrayList<YoutubeData> data){
        ArrayList<DisplayData> dispData = new ArrayList<DisplayData>();
        for(YoutubeData yData: data){
            ArrayList<Series> series = Categorize.categorize(yData.channelTitle, yData.videoTitles);
            if(series.isEmpty()){
                series.add(new Series(yData.channelTitle, "No Videos Found.", new ArrayList<Title>()));
            }
            dispData.add(new DisplayData(yData.channelTitle, series));
        }
        return dispData;
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

    private void createNewChannelIds(){
        channelIds = new ArrayList<String>();
        channelIds.add("UCVdtW2E4vwvf8yh4FY5us9A"); //SSoHPKC
        channelIds.add("UCV3kayetaucpObzusJbk9ag"); //Noah Baker
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
