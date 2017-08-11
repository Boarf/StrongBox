package com.example.leyom.strongbox.test;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import static android.R.attr.data;

/**
 * Created by Leyom on 08/08/2017.
 */

public class RecyclerViewData {
    String mIdentifier;
    String mPassword;
    String mUrl;
    ArrayList<RecyclerViewData> mDataList;

    public void setIdentifier(String identifier) {
        mIdentifier = identifier;
    }

    public void setPasswaord(String password) {
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

    public ArrayList<RecyclerViewData> getDataList() {
        return mDataList;
    }

    public void setAll(String identifier, String password, String url) {
        mIdentifier = identifier;
        mPassword = password;
        mUrl = url;
    }
    public String getPassword(int index) {
        return mDataList.get(index).getPassword();
    }
    public String getIdentifier(int index) {
        return mDataList.get(index).getIdentifier();
    }
    public String getUrl(int index) {
        return mDataList.get(index).getUrl();
    }

    public void setDataList () {

        RecyclerViewData a;
        mDataList = new ArrayList<RecyclerViewData>();
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Bozo","12345","galway.com");
        mDataList.add(a =new RecyclerViewData());
        a.setAll("Retro","azed","betelgeuse.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Futur","klmio","galway.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Armed","cvbc","akhénar.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Goose","78906","caiam.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Tango","wqasd","deneb.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Waltz","12345","eltanin.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Stags","yjgf","foramen.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Wolves","ezrzer","gomeisa.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Bravo","12345","Achernar.com");
        mDataList.add(a =new RecyclerViewData());
        a.setAll("Lima","azed","Fomalhaut.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Golf","klmio","Adhara.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Hotel","cvbc","akhénar.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Kilo","78906","Elnath.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Alpha","wqasd","Alioth.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Quebec","12345","Menkent.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Whiskey","yjgf","RasAlhague.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Mike","ezrzer","Schédar.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Delta","12345","Dschubba.com");
        mDataList.add(a =new RecyclerViewData());
        a.setAll("November","azed","Murzim.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Oscar","klmio","Polaris.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Uniform","cvbc","Wezen.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Charly","78906","Miaplacidus.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Foxtrot","wqasd","Alnilam.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("India","12345","Nunki.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("X-Ray","yjgf","Kochab.com");
        mDataList.add(a = new RecyclerViewData());
        a.setAll("Zulu","ezrzer","Pollux.com");

    }
}
