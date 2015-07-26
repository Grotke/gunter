package com.josephcmontgomery.gunter.tests;

import android.test.AndroidTestCase;

import com.josephcmontgomery.gunter.ExpandableListAdapter;

import java.util.ArrayList;

/**
 * Created by Joseph on 7/18/2015.
 */
public class AndroidAPITest extends AndroidTestCase {
    public void testIfExpandableListAdapter_WhenGivenEmptyObjects_IsEmpty() throws Exception {
        ExpandableListAdapter list = new ExpandableListAdapter(new ArrayList<String>(), new ArrayList<ArrayList<String>>());
        assertEquals(list.isEmpty(), true);
    }

    public void testIfExpandableListAdapter_WhenHaveOneParentButNoChildren_IsntEmpty() throws Exception{
        ArrayList<String> parent = new ArrayList<String>();
        ArrayList<ArrayList<String>> children = new ArrayList<ArrayList<String>>();
        parent.add("Seananners");
        ExpandableListAdapter list = new ExpandableListAdapter(parent, children);
        assertEquals(list.isEmpty(), false);
    }

    public void testIfExpandableListAdapter_WhenHasTwoGroups_ReturnsTwo() throws Exception{
        String[] parents = {"Seananners", "Quake"};
        ArrayList<String> parentList = prepareSingleList(parents);
        String[][] children = {{"Vid 1", "Vid 2"}, {"Vid 3","Vid 4"}};
        ArrayList<ArrayList<String>> childList = prepareDoubleList(children);

        ExpandableListAdapter list = new ExpandableListAdapter(parentList,childList);
        assertEquals(list.getGroupCount(),2);
    }

    public void testIfExpandableListAdapter_CountsChildrenCorrectly() throws Exception{
        String[] parents = {"Seananners", "Quake"};
        ArrayList<String> parentList = prepareSingleList(parents);
        String[][] children = {{"Vid 1", "Vid 2"}, {"Vid 3","Vid 4","Vid 5","Vid 6"}};
        ArrayList<ArrayList<String>> childList = prepareDoubleList(children);

        ExpandableListAdapter list = new ExpandableListAdapter(parentList,childList);
        assertEquals(list.getChildrenCount(1),4);
    }

    private ArrayList<String> prepareSingleList(String[] items){
        ArrayList<String> returnStrings = new ArrayList<String>(items.length);
        for(String item: items){
            returnStrings.add(item);
        }
        return returnStrings;
    }

    private ArrayList<ArrayList<String>> prepareDoubleList(String[][] items){
        ArrayList<ArrayList<String>> returnLists = new ArrayList<ArrayList<String>>(items.length);
        for(String[] stringList: items){
            returnLists.add(prepareSingleList(stringList));
        }
        return returnLists;
    }
}
