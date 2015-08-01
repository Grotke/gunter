package com.josephcmontgomery.gunter;

import android.text.format.DateUtils;

import com.google.api.client.util.DateTime;

/**
 * Keeps track of time related features like how old a video can be to be analyzed.
 * Created by Joseph on 7/31/2015.
 */
public class TimeKeeper {
    private static final long DEFAULT_OLDEST_VIDEO_TIME_FRAME = 2 * DateUtils.WEEK_IN_MILLIS;
    private static long oldestVideoTimeFrame = DEFAULT_OLDEST_VIDEO_TIME_FRAME;
    private TimeKeeper(){
    }

    static public void setOldestVideoTimeFrameByWeeks(long weeks){
        oldestVideoTimeFrame = weeks * DateUtils.WEEK_IN_MILLIS;
    }

    /**
     *
     * @param lastUpdateTime Unix time when channel uploads were last searched.
     * @param videoUploadTime Time video was uploaded.
     * @return Boolean telling whether video is recent enough to be included.
     */
    static public boolean ShouldGetVideo(long lastUpdateTime, String videoUploadTime){
        long currentTime = System.currentTimeMillis();
        long videoTime = DateTime.parseRfc3339(videoUploadTime).getValue();
        long timeSinceVideoUpload = currentTime - videoTime;
        long timeSinceAppUpdated = currentTime - lastUpdateTime;
        long lastCheck = oldestVideoTimeFrame;
        if(lastCheckedWithinTimeFrame(timeSinceAppUpdated)){
            lastCheck = timeSinceAppUpdated;
        }

        return videoUploadedAfterLastCheck(timeSinceVideoUpload, lastCheck);
    }

    static public boolean ShouldGetVideo(String videoUploadTime){
        return ShouldGetVideo(0, videoUploadTime);
    }

    static private boolean lastCheckedWithinTimeFrame(long timeSinceAppUpdated){
        return timeSinceAppUpdated < oldestVideoTimeFrame;
    }

    static private boolean videoUploadedAfterLastCheck(long timeSinceVideoUpload, long lastCheck){
        return timeSinceVideoUpload < lastCheck;
    }
}
