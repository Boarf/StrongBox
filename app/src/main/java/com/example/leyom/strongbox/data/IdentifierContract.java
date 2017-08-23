package com.example.leyom.strongbox.data;

import android.provider.BaseColumns;

/**
 * Created by Leyom on 22/08/2017.
 */

public class IdentifierContract {
    public static final class IdentifierEntry implements BaseColumns {
        public static final String TABLE_NAME ="identifiers";
        public static final String COLUMN_IDENTIFIER = "identifier";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_URL = "url";

    }
}
