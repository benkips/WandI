package com.ekarantechnologies.wandi.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.ekarantechnologies.wandi.Datafactory.Musicdatasourcefactory;
import com.ekarantechnologies.wandi.Datasource.Musicdatasource;
import com.ekarantechnologies.wandi.Utils.NetworkState;
import com.ekarantechnologies.wandi.models.Mydatamusic;

public class Musicviewmodel extends ViewModel {
    public LiveData<PagedList<Mydatamusic>> mydatamusicpagedlist;
    //search
    public MutableLiveData<String> filterTextAll = new MutableLiveData<>();
    //loading
    Musicdatasourcefactory musicdatasourcefactory;
    private LiveData<NetworkState> networkState;
    //initialloading
    private LiveData<NetworkState> initialnetworkState;

    public Musicviewmodel() {
        init();
    }
    private  void init(){
        PagedList.Config config=(new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(Musicdatasource.pagesize)
                .build();

        mydatamusicpagedlist= Transformations.switchMap(
                filterTextAll,input -> {
                    musicdatasourcefactory= new Musicdatasourcefactory(input);
                    networkState = Transformations.switchMap(musicdatasourcefactory.getMutableLiveData(),
                            dataSource -> dataSource.getNetworkState());
                    initialnetworkState=Transformations.switchMap(musicdatasourcefactory.getMutableLiveData(),
                            dataSource -> dataSource.getInitialLoading());
                    return (new LivePagedListBuilder(musicdatasourcefactory,config))
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
