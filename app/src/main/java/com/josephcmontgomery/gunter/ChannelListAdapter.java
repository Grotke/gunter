package com.josephcmontgomery.gunter;

import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Joseph on 9/14/2015.
 */
public class ChannelListAdapter implements ListAdapter {
    private ArrayList<DisplayData> data;
    private LayoutInflater inflater;

    public ChannelListAdapter(ArrayList<DisplayData> data){
        this.data = data;
        inflater = null;
    }

    public void setInflater(LayoutInflater inflater){
        this.inflater = inflater;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        String headerTitle = ((DisplayData) getItem(position)).channelTitle;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_group,null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer){

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer){

    }

    @Override
    public Object getItem(int position){
        return data.get(position);
    }

    @Override
    public boolean areAllItemsEnabled(){
        return true;
    }

    @Override
   public long getItemId(int position){
        return position;
    }

    @Override
    public boolean hasStableIds(){
        return false;
    }

    @Override
    public boolean isEmpty(){
        return data.isEmpty();
    }

    @Override
    public int getCount(){
        return data.size();
    }

    @Override
    public int getItemViewType(int position){
        return 0;
    }

    @Override
    public int getViewTypeCount(){
        return 1;
    }

    @Override
    public boolean isEnabled(int position){
        return true;
    }
}
