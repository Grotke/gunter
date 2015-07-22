package com.josephcmontgomery.gunter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    ArrayList<String> parents;
    ArrayList<ArrayList<String>> children;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expListView = (ExpandableListView) findViewById(R.id.channel_list);
        prepareListData();
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

    public void prepareListData(){
        prepareParents();
        prepareChildren();
    }

    public void prepareParents(){
        parents = new ArrayList<String>();
        parents.add("Seananners");
        parents.add("Quake");
    }

    public void prepareChildren(){
        children = new ArrayList<ArrayList<String>>();
        ArrayList<String> child = new ArrayList<String>();
        child.add("New vid 1");
        child.add("New vid 2");
        children.add(child);
        ArrayList<String> newChild = new ArrayList<String>();
        newChild.add("Quake vid 1");
        children.add(newChild);
    }
}
