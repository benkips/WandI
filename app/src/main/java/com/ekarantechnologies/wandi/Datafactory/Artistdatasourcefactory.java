package com.ekarantechnologies.wandi.Datafactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.ekarantechnologies.wandi.Datasource.Artistdatasource;
import com.ekarantechnologies.wandi.Datasource.Musicdatasource;

public class Artistdatasourcefactory extends DataSource.Factory {
    private MutableLiveData<Artistdatasource> mutableLiveData;

    private String searchKey;
    private Artistdatasource artistdatasource;


    public Artistdatasourcefactory(String searchKey) {
        this.searchKey = searchKey;
        this.mutableLiveData = new MutableLiveData<Artistdatasource>();
    }

    @NonNull
    @Override
    public DataSource create() {
        artistdatasource=new Artistdatasource(searchKey);
        mutableLiveData.postValue(artistdatasource);
        return artistdatasource;
    }
    public MutableLiveData<Artistdatasource> getMutableLiveData() {
        return mutableLiveData;
    }
}
