package com.ekarantechnologies.wandi.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.ekarantechnologies.wandi.Datafactory.Artistdatasourcefactory;
import com.ekarantechnologies.wandi.Datasource.Artistdatasource;
import com.ekarantechnologies.wandi.Utils.NetworkState;
import com.ekarantechnologies.wandi.models.Mydataartist;

public class Artistviewmodel extends ViewModel {
    public LiveData<PagedList<Mydataartist>> mydataartistpagedlist;
    //search
    public MutableLiveData<String> filterTextAll = new MutableLiveData<>();
    //loading
    Artistdatasourcefactory artistdatasourcefactory;
    private LiveData<NetworkState> networkState;
    //initialloading
    private LiveData<NetworkState> initialnetworkState;

    public Artistviewmodel() {
        init();
    }

    private void init() {
        PagedList.Config config=(new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(Artistdatasource.pagesize)
                .build();

        mydataartistpagedlist= Transformations.switchMap(
                filterTextAll,input -> {
                    artistdatasourcefactory= new Artistdatasourcefactory(input);
                    networkState = Transformations.switchMap(artistdatasourcefactory.getMutableLiveData(),
                            dataSource -> dataSource.getNetworkState());
                    initialnetworkState=Transformations.switchMap(artistdatasourcefactory.getMutableLiveData(),
                            dataSource -> dataSource.getInitialLoading());
                    return (new LivePagedListBuilder(artistdatasourcefactory,config))
                            .build();
                });

    }
    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }
    public LiveData<NetworkState> getInitialnetworkStateNetworkState() {
        return initialnetworkState;
    }
}
