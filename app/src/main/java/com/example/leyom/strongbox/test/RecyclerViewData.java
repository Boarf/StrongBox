package com.example.leyom.strongbox.test;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import static android.R.attr.data;
import static android.R.attr.password;

/**
 * Created by Leyom on 08/08/2017.
 */

public class RecyclerViewData {
    String mIdentifier;
    String mUsername;
    String mPassword;
    String mUrl;




    public void setIdentifier(String identifier) {
        mIdentifier = identifier;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getIdentifier() {
        return mIdentifier;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getUrl() {
        return mUrl;
    }



    public void setAll(String identifier, String userName, String password, String url) {
        mIdentifier = identifier;
        mUsername = userName;
        mPassword = password;
        mUrl = url;
    }



    public class RecyclerViewDataList {
        ArrayList<RecyclerViewData> mDataList;

        public ArrayList<RecyclerViewData> getDataList() {
            return mDataList;
        }

        public String getPassword(int index) {
            return mDataList.get(index).getPassword();
        }

        public String getIdentifier(int index) {
            return mDataList.get(index).getIdentifier();
        }

        public String getUsername(int index) {
            return mDataList.get(index).getUsername();
        }

        public String getUrl(int index) {
            return mDataList.get(index).getUrl();
        }

        public   RecyclerViewDataList() {


            RecyclerViewData a;
            mDataList = new ArrayList<RecyclerViewData>();
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Ambre", "Bozo", "12345", "galway.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Damburite", "Retro", "azed", "betelgeuse.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Howlite", "Futur", "klmio", "galway.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Emeraude", "Armed", "cvbc", "akhénar.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Cyanite", "Goose", "78906", "caiam.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Chrysocolle", "Tango", "wqasd", "deneb.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Dolomite", "Waltz", "12345", "eltanin.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Corail", "Stags", "yjgf", "foramen.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Calcite", "Wolves", "ezrzer", "gomeisa.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Bronzite", "Bravo", "12345", "Achernar.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Grenat", "Lima", "azed", "Fomalhaut.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Iolite", "Golf", "klmio", "Adhara.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Aventurine", "Hotel", "cvbc", "akhénar.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Argent", "Kilo", "78906", "Elnath.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("SHATTUKITE", "Alpha", "wqasd", "Alioth.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Apatite", "Quebec", "12345", "Menkent.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Jaspe", "Whiskey", "yjgf", "RasAlhague.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Kunzite", "Mike", "ezrzer", "Schédar.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Labradorite", "Delta", "12345", "Dschubba.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Grenat", "November", "azed", "Murzim.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Fluorite", "Oscar", "klmio", "Polaris.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Fossile", "Uniform", "cvbc", "Wezen.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Citrine", "Charly", "78906", "Miaplacidus.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Cornaline", "Foxtrot", "wqasd", "Alnilam.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("PREHNITE", "India", "12345", "Nunki.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("Turquoise", "X-Ray", "yjgf", "Kochab.com");
            mDataList.add(a = new RecyclerViewData());
            a.setAll("tanzanite", "Zulu", "ezrzer", "Pollux.com");

        }
    }
}
