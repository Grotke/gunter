package com.josephcmontgomery.gunter.tests;

import android.test.AndroidTestCase;
import android.text.format.DateUtils;

import com.google.api.client.util.DateTime;
import com.josephcmontgomery.gunter.TimeKeeper;

/**
 * Created by Joseph on 8/1/2015.
 */
public class TimeKeeperTest extends AndroidTestCase {
    public void testIfShouldGetVideo_GivenNoUpdateTimeAndTimeOlderThanDefaultTimeFrame_ReturnsFalse()throws Exception{
        assertFalse(TimeKeeper.ShouldGetVideo(DateTime.parseRfc3339("2015-07-01T04:32:34.000Z")));
    }

    public void testIfShouldGetVideo_GivenNoUpdateTimeAndVideoTimeWithinDefaultTimeFrame_ReturnsTrue() throws Exception{
        assertTrue(TimeKeeper.ShouldGetVideo(new DateTime(System.currentTimeMillis() - (DateUtils.DAY_IN_MILLIS * 3))));
    }

    public void testIfShouldGetVideo_GivenUpdateTimeAndVideoTimeWithinDefaultTimeFrame_ReturnsTrue() throws Exception{
        assertTrue(TimeKeeper.ShouldGetVideo(DateTime.parseRfc3339("2015-07-21T04:32:34.000Z"), DateTime.parseRfc3339("2015-07-31T04:32:34.000Z")));
    }

    public void testIfShouldGetVideo_GivenUpdateTimeAndTimeOlderThanDefaultTimeFrame_ReturnsFalse()throws Exception{
        assertFalse(TimeKeeper.ShouldGetVideo(DateTime.parseRfc3339("2015-07-01T04:32:34.000Z"), DateTime.parseRfc3339("2015-06-21T04:32:34.000Z")));
    }

    public void testIfShouldGetVideo_GivenOldUpdateTimeAndVideoTimeWithinDefaultTimeFrame_ReturnsTrue() throws Exception{
        assertTrue(TimeKeeper.ShouldGetVideo(DateTime.parseRfc3339("2015-07-01T04:32:34.000Z"), DateTime.parseRfc3339("2015-07-31T04:32:34.000Z")));
    }
}
