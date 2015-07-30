package com.josephcmontgomery.gunter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Joseph on 7/18/2015.
 */
//TODO Adapt this for series and channels.
public class ExpandableListAdapter extends BaseExpandableListAdapter{
    ArrayList<YoutubeData> data;
    ArrayList<String> parentItems;
    ArrayList<ArrayList<String>> childItems;
    LayoutInflater inflater;

    public ExpandableListAdapter(ArrayList<String> parents, ArrayList<ArrayList<String>> children){
        inflater = null;
        parentItems = parents;
        childItems = children;
    }

    public ExpandableListAdapter(ArrayList<YoutubeData> data){
        this.data = data;
        inflater = null;
    }

    public void setInflater(LayoutInflater inflater){
        this.inflater = inflater;
    }

    @Override
    //TODO throw exception when inflater isn't set
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition,childPosition);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public int getGroupCount() {
        //return parentItems.size();
        return data.size();
    }

    public void addParent(String parent, int position){
        parentItems.add(parent);
    }

    public void addChild(ArrayList<String> child, int position){
        childItems.add(child);
    }

    public void update(ArrayList<String> parents, ArrayList<ArrayList<String>> children){
        parentItems = parents;
        childItems = children;
        notifyDataSetChanged();
    }

    @Override
    //TODO Adapt this for video ids.
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //return childItems.get(groupPosition).size();
        return data.get(groupPosition).videoTitles.size();
    }

    @Override
    //TODO change this to true when ids are implemented.
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public Object getGroup(int groupPosition) {
        //return parentItems.get(groupPosition);
        return data.get(groupPosition).channelTitle;
    }

    @Override
    //TODO Adapt this for channel ids.
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_group,null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        //return childItems.get(groupPosition).get(childPosition);
        return data.get(groupPosition).videoTitles.get(childPosition);
    }
}
