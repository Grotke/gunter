package com.josephcmontgomery.gunter.tests;

import android.test.AndroidTestCase;
import android.util.Log;

import com.google.api.client.util.DateTime;

/**
 * Created by Joseph on 7/18/2015.
 */
public class AndroidAPITest extends AndroidTestCase {
    /*public void testIfExpandableListAdapter_WhenGivenEmptyObjects_IsEmpty() throws Exception {
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
        ArrayList<YoutubeData> data = new ArrayList<YoutubeData>(2);
        data.add(prepareYoutubeData("Seananners", ))
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

    private YoutubeData prepareYoutubeData(String channel, String[] videoTitles){
        return new YoutubeData(channel, new ArrayList<String>(videoTitles));
    }*/

    public void testIfDateTime_WhenGivenDateString_HasValueGreaterThanZero() throws Exception{
        DateTime date = DateTime.parseRfc3339("2015-07-31T04:32:34.000Z");
        Log.d("VALUE", String.valueOf(date.getValue()));
        assertTrue(date.getValue() > 0);
    }

    public void testIfDateTime_WhenConvertsToRFC_ReturnsInputStringWhenConvertBack() throws Exception{
        DateTime date = DateTime.parseRfc3339("2015-07-31T04:32:34.000Z");
        Log.d("RFC STRING", date.toStringRfc3339());
        Log.d("NORMAL STRING", date.toString());
        assertEquals(date.toStringRfc3339(), "2015-07-31T04:32:34.000Z");
    }

    public void testIfDateTime_WhenGivenDifferentDateSpans_ReturnsSameValue() throws Exception{
        DateTime date1 = DateTime.parseRfc3339("2015-07-31T04:32:34.000Z");
        DateTime earlierDate1 = DateTime.parseRfc3339("2015-07-17T04:32:34.000Z");
        DateTime date2 = DateTime.parseRfc3339("2012-04-17T04:32:34.000Z");
        DateTime earlierDate2 = DateTime.parseRfc3339("2012-04-03T04:32:34.000Z");
        long twoWeekValue1 = date1.getValue() - earlierDate1.getValue();
        long twoWeekValue2 = date2.getValue() - earlierDate2.getValue();
        assertEquals(twoWeekValue1,twoWeekValue2);
    }
}
