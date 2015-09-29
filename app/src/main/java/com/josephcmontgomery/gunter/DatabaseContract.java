package com.josephcmontgomery.gunter;

import android.provider.BaseColumns;

/**
 * Created by Joseph on 9/29/2015.
 */
public final class DatabaseContract {

    public DatabaseContract(){};

    public static abstract class Channel implements BaseColumns{
        public static final String TABLE_NAME = "channel";
        public static final String COLUMN_NAME_CHANNEL_ID = "channel_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SUBSCRIBED = "subscribed";
    }

    public static abstract class Series implements BaseColumns{
        public static final String TABLE_NAME = "series";
        public static final String COLUMN_NAME_SERIES_ID = "series_id";
        public static final String COLUMN_NAME_CHANNEL_ID = "channel_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_IGNORED = "ignored";
    }

    public static abstract class Video implements BaseColumns{
        public static final String TABLE_NAME = "video";
        public static final String COLUMN_NAME_VIDEO_ID = "video_id";
        public static final String COLUMN_NAME_CHANNEL_ID = "channel_id";
        public static final String COLUMN_NAME_SERIES_ID = "series_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_VIEWED = "viewed";
    }

    public static abstract class Chunk implements BaseColumns{
        public static final String TABLE_NAME = "chunk";
        public static final String COLUMN_NAME_CHANNEL_ID = "channel_id";
        public static final String COLUMN_NAME_VIDEO_ID = "video_id";
        public static final String COLUMN_NAME_TITLE = "title";
    }

    public static abstract class Stat implements BaseColumns{
        public static final String TABLE_NAME = "stat";
        public static final String COLUMN_NAME_CHANNEL_ID = "channel_id";
        public static final String COLUMN_NAME_CHUNK_0 = "chunk_0";
        public static final String COLUMN_NAME_CHUNK_1 = "chunk_1";
        public static final String COLUMN_NAME_CHUNK_2 = "chunk_2";
        public static final String COLUMN_NAME_CHUNK_3 = "chunk_3";
        public static final String COLUMN_NAME_CHUNK_4 = "chunk_4";
        public static final String COLUMN_NAME_CHUNK_5 = "chunk_5";
        public static final String COLUMN_NAME_CHUNK_6 = "chunk_6";
        public static final String COLUMN_NAME_CHUNK_7 = "chunk_7";
        public static final String COLUMN_NAME_CHUNK_8 = "chunk_8";
        public static final String COLUMN_NAME_CHUNK_9 = "chunk_9";
    }
}
