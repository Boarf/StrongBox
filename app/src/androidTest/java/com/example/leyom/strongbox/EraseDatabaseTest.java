package com.example.leyom.strongbox;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.leyom.strongbox.data.IdentifierContract;
import com.example.leyom.strongbox.data.IdentifierDbHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Leyom on 20/09/2017.
 */

@RunWith(AndroidJUnit4.class)

public class EraseDatabaseTest {

     @Test
     public void erase() {
         Context context = InstrumentationRegistry.getTargetContext();
         IdentifierDbHelper dbHelper = new IdentifierDbHelper(context);

         dbHelper.getWritableDatabase().delete(
            IdentifierContract.IdentifierEntry.TABLE_NAME,
            null,
            null
            );

     }


}
