package com.ekarantechnologies.wandi.Datafactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.ekarantechnologies.wandi.Datasource.Artistmusicdatasource;


public class Artistmusicdatasourcefactory extends DataSource.Factory{
    private MutableLiveData<Artistmusicdatasource> mutableLiveData;

    private String searchKey;
    private int musicid;
    private Artistmusicdatasource artistmusicdatasource;

    public Artistmusicdatasourcefactory(String searchKey, int musicid) {
        this.searchKey = searchKey;
        this.musicid = musicid;
        this.mutableLiveData = new MutableLiveData<Artistmusicdatasource>();
    }

    @NonNull
    @Override
    public DataSource create() {
        artistmusicdatasource=new Artistmusicdatasource(searchKey,musicid);
        mutableLiveData.postValue(artistmusicdatasource);
        return artistmusicdatasource;
    }

    public MutableLiveData<Artistmusicdatasource> getMutableLiveData() {
        return mutableLiveData;
    }
}
