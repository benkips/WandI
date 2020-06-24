package com.ekarantechnologies.wandi.Datafactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.ekarantechnologies.wandi.Datasource.Musicdatasource;

public class Musicdatasourcefactory extends DataSource.Factory {
    private MutableLiveData<Musicdatasource> mutableLiveData;

    private String searchKey;
    private Musicdatasource musicdatasource;

    public Musicdatasourcefactory(String searchKey) {
        this.searchKey = searchKey;
        this.mutableLiveData = new MutableLiveData<Musicdatasource>();
    }

    @NonNull
    @Override
    public DataSource create() {
        musicdatasource=new Musicdatasource(searchKey);
        mutableLiveData.postValue(musicdatasource);
        return musicdatasource;
    }
    public MutableLiveData<Musicdatasource> getMutableLiveData() {
        return mutableLiveData;
    }
}
