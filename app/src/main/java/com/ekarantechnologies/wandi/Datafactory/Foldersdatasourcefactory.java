package com.ekarantechnologies.wandi.Datafactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.ekarantechnologies.wandi.Datasource.Folderdatasource;

public class Foldersdatasourcefactory extends DataSource.Factory {
    private MutableLiveData<Folderdatasource> mutableLiveData;

    private String searchKey;
    private Folderdatasource folderdatasource;

    public Foldersdatasourcefactory(String searchKey) {
        this.searchKey = searchKey;
        this.mutableLiveData = new MutableLiveData<Folderdatasource>();
    }

    @NonNull
    @Override
    public DataSource create() {
        folderdatasource=new Folderdatasource(searchKey);
        mutableLiveData.postValue(folderdatasource);

        return folderdatasource;
    }
    public MutableLiveData<Folderdatasource> getMutableLiveData() {
        return mutableLiveData;
    }
}
