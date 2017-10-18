package com.example.leyom.strongbox.serialization;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import static android.R.attr.id;

/**
 * Created by Leyom on 11/09/2017.
 */

public class Identifier implements Comparable<Identifier>, Parcelable{
    private String mIdentifier;
    private String mUsername;
    private String mPassword;
    private String mUrl;
    private int mId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mIdentifier);
        dest.writeString(mUsername);
        dest.writeString(mPassword);
        dest.writeString(mUrl);
        dest.writeInt(mId);
    }

    public static final Parcelable.Creator<Identifier> CREATOR =
            new Parcelable.Creator<Identifier>() {
                @Override
                public Identifier createFromParcel(Parcel source) {
                    return new Identifier(source);
                }

                @Override
                public Identifier[] newArray(int size) {
                    return new Identifier[size];
                }
            };

    private Identifier(Parcel in)
    {
        mIdentifier = in.readString();
        mUsername = in.readString();
        mPassword = in.readString();
        mUrl = in.readString();
        mId = in.readInt();
    }
    public Identifier() {

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
