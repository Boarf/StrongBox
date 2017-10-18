package com.example.leyom.strongbox;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.leyom.strongbox.serialization.Identifier;
import com.example.leyom.strongbox.serialization.SerializedIdentifier;

import java.util.ArrayList;

import static com.example.leyom.strongbox.SyncConstantsReceiver.BACKUP_TO_STORAGE_EXTRA;

/**
 * Created by Leyom on 13/10/2017.
 */

public class SyncService extends IntentService {
    public static final String SYNC_TO_STORAGE_EXTRA = "sync to storage";
    public static final String DATA_TO_SYNC_EXTRA = "data to sync";

    public SyncService() {
        super("syncService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        boolean syncToStorage = intent.getBooleanExtra(SYNC_TO_STORAGE_EXTRA,false);
        ArrayList<Identifier> identifiers ;
        // Intent for the local broadcast receiver
        Intent localIntent = new Intent(SyncConstantsReceiver.SYNC_BROADCAST_ACTION);

        // The SerializedIdentifier is a singleton
        SerializedIdentifier serializedIdentifiers = SerializedIdentifier.getInstance(getApplicationContext());
        if(syncToStorage){
            // Save the identifiers list to storage
            identifiers = intent.getParcelableArrayListExtra(DATA_TO_SYNC_EXTRA);
            serializedIdentifiers.writeJsonStream(identifiers);


        }else {
            // We synchronize from storage
            // We must return the list of identifiers to the UI thread through a broadcast receiver
            identifiers = (ArrayList<Identifier>) serializedIdentifiers.readJsonStream();

            localIntent.putParcelableArrayListExtra(SyncConstantsReceiver.IDENTIFIERS_BROADCAST_EXTRA,
                    identifiers);


        }

        // We indicate back which operation was ongoing
        // Beware that even if it is the same purpose as SYNC_TO_STORAGE for the service
        // We must define a string constant with a different value for brodcast receiver
        // otherwise there is an error of unmarshalling
        localIntent.putExtra(BACKUP_TO_STORAGE_EXTRA,syncToStorage);
        // Send local broadcast to indicate that the Service is done
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

    }
}
