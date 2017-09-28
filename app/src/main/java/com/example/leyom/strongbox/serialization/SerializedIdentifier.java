package com.example.leyom.strongbox.serialization;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import android.util.Log;

import com.example.leyom.strongbox.IdentifierAdapter;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Leyom on 08/09/2017.
 */

public class SerializedIdentifier {

    private static final String TAG = "SerializedIdentifier";


    private Context mContext;
    private String mProvider;
    private String mProviderUsername;
    private String mOwnername;
    private String mFileName;

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        mFileName = fileName;
    }


    public  SerializedIdentifier (Context context) {
        mContext = context;
        Cursor c = mContext.getContentResolver().query(ContactsContract.Profile.CONTENT_URI,
                null,null,null,null);
        if(c.moveToFirst()) {
            mOwnername = c.getString(c.getColumnIndex("display_name"));
            Log.d(TAG, "SerializedIdentifier: owner " + mOwnername);
        }
        c.close();
    }


    public void setProviderUsername(String providerUsername) {
        mProviderUsername = providerUsername;
    }

    public void setProvider(String provider) {
        mProvider = provider;
    }

    public List<Identifier> readJsonStream ()  {

        if (mProvider==null || mProviderUsername==null ) {
            return null;
        }

        mFileName = "strongbox" + "_" + mProvider + "_" + mOwnername + "_" + mProviderUsername;
        List<Identifier> identifiersList = new ArrayList<Identifier>();
        try {
            FileInputStream read = mContext.openFileInput(mFileName);
            //FileInputStream read = new FileInputStream(fileName);
            InputStreamReader in = new InputStreamReader(read);

            JsonReader reader = new JsonReader(in);
            Gson gson = new Gson();

            reader.beginArray();
            while (reader.hasNext()) {
                Identifier identifier = gson.fromJson(reader, Identifier.class);
                identifiersList.add(identifier);

            }
            reader.endArray();
            reader.close();
            in.close();
            read.close();


        } catch (FileNotFoundException e) {
            Log.d(TAG, "readJsonStream: " + mFileName + "not found");

        } catch (IOException e) {
            Log.d(TAG, "readJsonStream: " + e.getMessage());
        }
        return identifiersList;

    }

    public boolean appendJsonStream( Identifier identifier) throws IOException {

        if (mProvider==null || mProviderUsername==null ) {
            return false;
        }

        mFileName = "strongbox" + "_" + mProvider + "_" +mOwnername + "_" + mProviderUsername;

        try {
            FileOutputStream write = mContext.openFileOutput(mFileName, mContext.MODE_PRIVATE | mContext.MODE_APPEND);
            OutputStreamWriter out = new OutputStreamWriter(write);
            JsonWriter writer = new JsonWriter(out);
            Gson gson = new Gson();
            writer.beginArray();
            gson.toJson(identifier, Identifier.class, writer);
            writer.endArray();
            writer.close();
            out.close();
            write.close();
            return true;
        }
        catch(FileNotFoundException e) {
            return false;
        }
        catch (IOException e) {
            return false;
        }
    }

    public boolean writeJsonStream( List<Identifier> identifiersList) {

        if (mProvider==null || mProviderUsername==null ) {
            return false;
        }

        mFileName = "strongbox" + "_" + mProvider + "_" +mOwnername + "_" + mProviderUsername;
        try {
            FileOutputStream write = mContext.openFileOutput(mFileName, mContext.MODE_PRIVATE);
            //FileOutputStream write = new FileOutputStream(fileName);
            OutputStreamWriter out = new OutputStreamWriter(write);
            JsonWriter writer = new JsonWriter(out);
            Gson gson = new Gson();
            writer.beginArray();

            for (Identifier identifier : identifiersList) {
                gson.toJson(identifier, Identifier.class, writer);
            }
            writer.endArray();
            writer.close();
            out.close();
            write.close();
            return true;
        }
        catch(FileNotFoundException e) {
            Log.d(TAG, "writeJsonStream: "  + mFileName + "not found");
            return false;
        }
        catch(IOException e) {
            Log.d(TAG, "writeJsonStream: " + e.getMessage());
            return false;
        }
    }


}
