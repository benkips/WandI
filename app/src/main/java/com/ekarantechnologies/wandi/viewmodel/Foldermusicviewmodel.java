package com.ekarantechnologies.wandi.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.ekarantechnologies.wandi.Datafactory.Artistmusicdatasourcefactory;
import com.ekarantechnologies.wandi.Datafactory.Foldermusicdatasourcefactory;
import com.ekarantechnologies.wandi.Datasource.Artistdatasource;
import com.ekarantechnologies.wandi.Utils.NetworkState;
import com.ekarantechnologies.wandi.models.Mydatamusicfolder;

public class Foldermusicviewmodel extends ViewModel {
    public LiveData<PagedList<Mydatamusicfolder>> mydatamusicfolderpagedlist;
    //search
    public MutableLiveData<String> filterTextAll = new MutableLiveData<>();
    //loading
    Foldermusicdatasourcefactory foldermusicdatasourcefactory;
    private LiveData<NetworkState> networkState;
    //initialloading
    private LiveData<NetworkState> initialnetworkState;


    public Foldermusicviewmodel() {
        init();
    }
    private void init() {
        PagedList.Config config=(new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(Artistdatasource.pagesize)
                .build();

        mydatamusicfolderpagedlist= Transformations.switchMap(
                filterTextAll,input -> {
                    String[] separated = input.split("\\^");
                    separated[0] = separated[0].trim();
                    if(separated.length==0){
                        input="";
                        foldermusicdatasourcefactory= new Foldermusicdatasourcefactory(input, Integer.valueOf(separated[1]));
                    }else {
                        separated[0] = separated[0].trim(); // this will contain "Fruit"
                        separated[1] = separated[1].trim();
                        foldermusicdatasourcefactory= new Foldermusicdatasourcefactory( separated[0], Integer.valueOf(separated[1]));
                    }

                    networkState = Transformations.switchMap(foldermusicdatasourcefactory.getMutableLiveData(),
                            dataSource -> dataSource.getNetworkState());
                    initialnetworkState=Transformations.switchMap(foldermusicdatasourcefactory.getMutableLiveData(),
                            dataSource -> dataSource.getInitialLoading());
                    return (new LivePagedListBuilder(foldermusicdatasourcefactory,config))
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
