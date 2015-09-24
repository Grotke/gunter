package com.josephcmontgomery.gunter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.josephcmontgomery.gunter.categorize.Series;

import java.util.ArrayList;

public class SeriesDisplayActivity extends AppCompatActivity {
    ExpandableListView expListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_display);
        ArrayList<Series> series = (ArrayList)getIntent().getSerializableExtra("series");
        setTitle(getIntent().getStringExtra("channel_title"));
        setUpViewsAndAdapters(series);
    }

    private void setUpViewsAndAdapters(ArrayList<Series> series){
        expListView = (ExpandableListView) findViewById(R.id.series_list);
        SeriesExpandableListAdapter listAdapter = new SeriesExpandableListAdapter(series);
        listAdapter.setInflater((LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        expListView.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_series_display, menu);
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
}
