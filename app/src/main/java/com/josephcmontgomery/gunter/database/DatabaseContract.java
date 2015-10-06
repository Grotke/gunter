package com.josephcmontgomery.gunter.database;

import android.provider.BaseColumns;

/**
 * Created by Joseph on 9/29/2015.
 */
public final class DatabaseContract {

    public DatabaseContract(){};

    public static abstract class Channel implements BaseColumns{
        public static final String TABLE_NAME = "channel";
        public static final String COLUMN_NAME_YOUTUBE_CHANNEL_ID = "youtube_channel_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SUBSCRIBED = "subscribed";
        public static final String COLUMN_NAME_CHUNK_STATS = "chunk_stats";
    }

    public static abstract class Series implements BaseColumns{
        public static final String TABLE_NAME = "series";
        public static final String COLUMN_NAME_CHANNEL_ID = "channel_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_IGNORED = "ignored";
    }

    public static abstract class Video implements BaseColumns{
        public static final String TABLE_NAME = "video";
        public static final String COLUMN_NAME_CHANNEL_ID = "channel_id";
        public static final String COLUMN_NAME_SERIES_ID = "series_id";
        public static final String COLUMN_NAME_YOUTUBE_VIDEO_ID = "youtube_video_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_VIEWED = "viewed";
    }

    public static abstract class Chunk implements BaseColumns{
        public static final String TABLE_NAME = "chunk";
        public static final String COLUMN_NAME_CHANNEL_ID = "channel_id";
        public static final String COLUMN_NAME_VIDEO_ID = "video_id";
        public static final String COLUMN_NAME_TITLE = "title";
    }
}
