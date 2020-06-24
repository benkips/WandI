package com.ekarantechnologies.wandi.Datasource;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.ekarantechnologies.wandi.Network.ApiClient;
import com.ekarantechnologies.wandi.Network.ApiInterface;
import com.ekarantechnologies.wandi.Utils.NetworkState;
import com.ekarantechnologies.wandi.models.Mydatamusic;
import com.ekarantechnologies.wandi.models.music;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Musicdatasource extends PageKeyedDataSource<Integer, Mydatamusic> {
    private static ApiInterface apiInterface;
    public static final int pagesize = 10;
    public static final int firstpage = 1;
    public String dataz;


    private MutableLiveData networkState;
    private MutableLiveData initialLoading;

    public Musicdatasource(String dataz) {
        this.dataz = dataz;
        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Mydatamusic> callback) {

        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<music>> call = apiInterface.getmusic(firstpage, pagesize, dataz);
        call.enqueue(new Callback<List<music>>() {
            @Override
            public void onResponse(Call<List<music>> call, Response<List<music>> response) {
                if (response.body()!= null) {
                    if(response.body().get(0).status.equals("ok")){
                    callback.onResult(response.body().get(2).mydata, null, firstpage + 1);
                    initialLoading.postValue(NetworkState.LOADED);
                    networkState.postValue(NetworkState.LOADED);
                    }else{
                     initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED,response.body().get(0).status));
                     networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.body().get(0).status));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<music>> call, Throwable t) {
                if (t instanceof HttpException) {
                    // We had non-2XX http error
                    String errorMessage = "http error";
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED,errorMessage));


                } else if (t instanceof IOException) {
                    // A network or conversion error happened
                    String errorMessage = "Check your internet connection";
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED,errorMessage));
                } else {
                    String errorMessage = "Something  went wrong";
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED,errorMessage));
                }
               /* String errorMessage = t == null ? "unknown error" : t.getMessage();
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));*/

            }
        });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Mydatamusic> callback) {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<music>> call = apiInterface.getmusic(params.key, pagesize, dataz);
        call.enqueue(new Callback<List<music>>() {
            @Override
            public void onResponse(Call<List<music>> call, Response<List<music>> response) {
                Log.i(TAG, "Loading Rang " + params.key + " Count " + params.requestedLoadSize);


                if (response.body() != null) {
                    Integer key = (params.key > 1) ? params.key - 1 : null;
                    callback.onResult(response.body().get(2).mydata, key);
                }
            }

            @Override
            public void onFailure(Call<List<music>> call, Throwable t) {
            }
        });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Mydatamusic> callback) {

        networkState.postValue(NetworkState.LOADING);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<music>> call = apiInterface.getmusic(params.key, pagesize, dataz);
        call.enqueue(new Callback<List<music>>() {
            @Override
            public void onResponse(Call<List<music>> call, Response<List<music>> response) {
                Log.i(TAG, "Loading Range " + params.key + " Count " + params.requestedLoadSize);
                if (response.body() != null) {
                    Integer key = (response.body().get(1).total >= params.key) ? params.key + 1 : null;
                    if(key!=null)  callback.onResult(response.body().get(2).mydata, key);
                    networkState.postValue(NetworkState.LOADED);
                }else {
                    /* initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));*/
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<music>> call, Throwable t) {
                if (t instanceof HttpException) {
                    // We had non-2XX http error
                    String errorMessage = "Check your internet connection";
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));

                } else if (t instanceof IOException) {
                    // A network or conversion error happened
                    String errorMessage = "Check your internet connection";
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                } else {
                    String errorMessage = "Something  went wrong";
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                }
            }
        });
    }

}