package com.example.leyom.strongbox.serialization;

import android.support.annotation.NonNull;

import static android.R.attr.id;

/**
 * Created by Leyom on 11/09/2017.
 */

public class Identifier implements Comparable<Identifier>{
    private String mIdentifier;
    private String mUsername;
    private String mPassword;
    private String mUrl;
    private int mId;

    public Identifier()
    {

    }
    public Identifier(String identifier, String username, String password, String url)
    {
        setIdentifier(identifier);
        setUsername(username);
        setPassword(password);
        setUrl(url);
        setId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identifier that = (Identifier) o;

        if (mId != that.mId) return false;
        if (mIdentifier != null ? !mIdentifier.equals(that.mIdentifier) : that.mIdentifier != null)
            return false;
        if (mUsername != null ? !mUsername.equals(that.mUsername) : that.mUsername != null)
            return false;
        if (mPassword != null ? !mPassword.equals(that.mPassword) : that.mPassword != null)
            return false;
        return mUrl != null ? mUrl.equals(that.mUrl) : that.mUrl == null;

    }


    public String getIdentifier() {
        return mIdentifier;
    }

    public void setIdentifier(String identifier) {
        mIdentifier = identifier;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    @Override
    public int compareTo(@NonNull Identifier o) {
        return mId - o.getId();
    }
}
