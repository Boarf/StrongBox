package com.example.leyom.strongbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.example.leyom.strongbox.serialization.Identifier;

import java.util.ArrayList;

import static com.example.leyom.strongbox.SyncConstantsReceiver.BACKUP_TO_STORAGE_EXTRA;
import static com.example.leyom.strongbox.SyncService.SYNC_TO_STORAGE_EXTRA;

/**
 * Created by Leyom on 16/10/2017.
 */

public class SyncReceiver extends BroadcastReceiver  {

    CompleteSync mCompleteSync;

    public interface CompleteSync {
        void finishSync(ArrayList<Identifier> readFromStorage);
    }
    public SyncReceiver(CompleteSync completeSync  ){
        mCompleteSync = completeSync;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getBooleanExtra(BACKUP_TO_STORAGE_EXTRA,false) == false) {
            ArrayList<Identifier> listFromStorage;
            listFromStorage = intent.getParcelableArrayListExtra(SyncConstantsReceiver.IDENTIFIERS_BROADCAST_EXTRA);
            mCompleteSync.finishSync(listFromStorage);
        }
        else {
            Toast.makeText(context,"Identifers list is synchronized",Toast.LENGTH_LONG);
        }
    }
}
