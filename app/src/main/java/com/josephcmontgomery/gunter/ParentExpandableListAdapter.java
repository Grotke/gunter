package com.josephcmontgomery.gunter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Joseph on 7/18/2015.
 */
//TODO Adapt this for series and channels.
public class ParentExpandableListAdapter extends BaseExpandableListAdapter{
    private ArrayList<DisplayData> data;
    private LayoutInflater inflater;
    private Context context;

    public ParentExpandableListAdapter(Context context, ArrayList<DisplayData> data){
        this.data = data;
        this.context = context;
        inflater = null;
    }

    public void setInflater(LayoutInflater inflater){
        this.inflater = inflater;
    }

    @Override
    //TODO throw exception when inflater isn't set
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //final String childText = (String) getChild(groupPosition,childPosition);
        ExpandableListView view = new ExpandableListView(context);
        view.setAdapter((ChildExpandableListAdapter) getChild(groupPosition, childPosition));
        view.setGroupIndicator(null);


        //SecondLevelexplv.setAdapter(new SecondLevelAdapter());

        //SecondLevelexplv.setGroupIndicator(null);

        //return SecondLevelexplv;

        /*if(convertView == null){
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);
        return convertView;*/
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    /*public void addData(YoutubeData data){
        this.data.add(data);
        notifyDataSetChanged();
    }

    public void update(ArrayList<YoutubeData> data){
        this.data = data;
        notifyDataSetChanged();
    }*/

    @Override
    //TODO Adapt this for video ids.
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
                //data.get(groupPosition).seriesList.size();
    }

    @Override
    //TODO change this to true when ids are implemented.
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public Object getGroup(int groupPosition) {
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
        ChildExpandableListAdapter adapt = new ChildExpandableListAdapter(data.get(groupPosition).seriesList);
        adapt.setInflater(inflater);
        return adapt;
    }
}
