package com.example.leyom.strongbox.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Leyom on 22/08/2017.
 */

public class IdentifierContract {
    public static final String CONTENT_AUTHORITY ="com.example.leyom.strongbox";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_IDENTIFIER_TABLE="identifier";

    public static final class IdentifierEntry implements BaseColumns {

        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_IDENTIFIER_TABLE)
                .build();

        public static final String TABLE_NAME ="identifiers";
        public static final String COLUMN_IDENTIFIER = "identifier";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_URL = "url";

    }
}
