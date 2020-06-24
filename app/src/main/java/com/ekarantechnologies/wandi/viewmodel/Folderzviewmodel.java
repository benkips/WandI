package com.ekarantechnologies.wandi.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.ekarantechnologies.wandi.Datafactory.Artistdatasourcefactory;
import com.ekarantechnologies.wandi.Datafactory.Foldersdatasourcefactory;
import com.ekarantechnologies.wandi.Datasource.Artistdatasource;
import com.ekarantechnologies.wandi.Utils.NetworkState;
import com.ekarantechnologies.wandi.models.Mydatafolders;

public class Folderzviewmodel extends ViewModel {
    public LiveData<PagedList<Mydatafolders>> mydatafolderspagedlist;
    //search
    public MutableLiveData<String> filterTextAll = new MutableLiveData<>();
    //loading
   Foldersdatasourcefactory foldersdatasourcefactory;
    private LiveData<NetworkState> networkState;
    //initialloading
    private LiveData<NetworkState> initialnetworkState;

    public Folderzviewmodel() {
        init();
    }

    private void init() {
        PagedList.Config config=(new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(Artistdatasource.pagesize)
                .build();

        mydatafolderspagedlist= Transformations.switchMap(
                filterTextAll,input -> {
                    foldersdatasourcefactory= new Foldersdatasourcefactory(input);
                    networkState = Transformations.switchMap(foldersdatasourcefactory.getMutableLiveData(),
                            dataSource -> dataSource.getNetworkState());
                    initialnetworkState=Transformations.switchMap(foldersdatasourcefactory.getMutableLiveData(),
                            dataSource -> dataSource.getInitialLoading());
                    return (new LivePagedListBuilder(foldersdatasourcefactory,config))
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
