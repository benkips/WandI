package com.ekarantechnologies.wandi.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.ekarantechnologies.wandi.Datafactory.Artistmusicdatasourcefactory;
import com.ekarantechnologies.wandi.Datasource.Artistdatasource;
import com.ekarantechnologies.wandi.Utils.NetworkState;
import com.ekarantechnologies.wandi.models.Mydatamusicartist;

public class Artistmusicviewmodel extends ViewModel {
    public LiveData<PagedList<Mydatamusicartist>> mydatamusicartistpagedlist;
    //search
    public MutableLiveData<String> filterTextAll = new MutableLiveData<>();
    //loading
    Artistmusicdatasourcefactory artistdatasourcefactory;
    private LiveData<NetworkState> networkState;
    //initialloading
    private LiveData<NetworkState> initialnetworkState;


    public Artistmusicviewmodel() {
      init();

    }

    private void init() {
        PagedList.Config config=(new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(Artistdatasource.pagesize)
                .build();

        mydatamusicartistpagedlist= Transformations.switchMap(
                filterTextAll,input -> {
                    String[] separated = input.split("\\^");
                    separated[0] = separated[0].trim();
                    if(separated.length==0){
                        input="";
                        artistdatasourcefactory= new Artistmusicdatasourcefactory(input, Integer.valueOf(separated[1]));
                    }else {
                        separated[0] = separated[0].trim(); // this will contain "Fruit"
                        separated[1] = separated[1].trim();
                        artistdatasourcefactory= new Artistmusicdatasourcefactory( separated[0], Integer.valueOf(separated[1]));
                    }

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

