package com.ekarantechnologies.wandi.Datafactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.ekarantechnologies.wandi.Datasource.Foldermusicdatasource;

public class Foldermusicdatasourcefactory extends DataSource.Factory{
    private MutableLiveData<Foldermusicdatasource> mutableLiveData;

    private String searchKey;
    private int musicid;
    private Foldermusicdatasource foldermusicdatasource;

    public Foldermusicdatasourcefactory(String searchKey, int musicid) {
        this.searchKey = searchKey;
        this.musicid = musicid;
        this.mutableLiveData = new MutableLiveData<Foldermusicdatasource>();
    }

    @NonNull
    @Override
    public DataSource create() {
        foldermusicdatasource=new Foldermusicdatasource(searchKey,musicid);
        mutableLiveData.postValue(foldermusicdatasource);
        return foldermusicdatasource;
    }
    public MutableLiveData<Foldermusicdatasource> getMutableLiveData() {
        return mutableLiveData;
    }
}
