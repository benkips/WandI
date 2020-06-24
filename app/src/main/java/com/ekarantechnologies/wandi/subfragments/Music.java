package com.ekarantechnologies.wandi.subfragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ekarantechnologies.wandi.R;
import com.ekarantechnologies.wandi.Utils.NetworkState;
import com.ekarantechnologies.wandi.adapters.Musicadapter;
import com.ekarantechnologies.wandi.databinding.FragmentMusicBinding;
import com.ekarantechnologies.wandi.models.Mydatamusic;
import com.ekarantechnologies.wandi.viewmodel.Musicviewmodel;
import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Music#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Music extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //view binding
    FragmentMusicBinding binding;
    Musicviewmodel musicviewmodel;
    Musicadapter musicadapter;

    public Music() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Music.
     */
    // TODO: Rename and change types and number of parameters
    public static Music newInstance(String param1, String param2) {
        Music fragment = new Music();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentMusicBinding.inflate(getLayoutInflater());
        return  binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        musicviewmodel=new ViewModelProvider(this).get(Musicviewmodel.class);
        //recycleview
        binding.rvmusic.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.rvmusic.setLayoutManager(manager);
        //adapter
         musicadapter=new Musicadapter(getContext());


        binding.srchtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString().trim();
                musicviewmodel.filterTextAll.setValue(query);

            }
        });
        binding.srchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = binding.srchtxt.getText().toString().trim();
                musicviewmodel.filterTextAll.setValue(query);
            }
        });
        //begin to searvh
        musicviewmodel.filterTextAll.setValue("");
        musicviewmodel.mydatamusicpagedlist.observe(getViewLifecycleOwner(), new Observer<PagedList<Mydatamusic>>() {
            @Override
            public void onChanged(PagedList<Mydatamusic> mydatamusics) {
                initialloading();
                loading();

                musicadapter.submitList(mydatamusics);
            }
        });
        binding.rvmusic.setAdapter(musicadapter);

    }
    private  void loading(){
        musicviewmodel.getNetworkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
                /*Toast.makeText(getContext(), networkState.getMsg(), Toast.LENGTH_SHORT).show();
                Snackbar.make(binding.getRoot(),networkState.getMsg(),Snackbar.LENGTH_LONG).show();*/
                musicadapter.setNetworkState(networkState);


            }
        });
    }
    private  void initialloading(){
        musicviewmodel.getInitialnetworkStateNetworkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
                if ((networkState.getMsg().equals("Running"))) {
                    binding.txtError.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.VISIBLE);
                } else if ((networkState.getMsg().equals("Success"))) {
                    binding.txtError.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.GONE);
                }else if ((networkState.getMsg().contains("internet"))){
                    binding.progressBar.setVisibility(View.GONE);
                    binding.txtError.setVisibility(View.VISIBLE);
                    binding.txtError.setText("check your internet connection \n click to retry");
                    binding.txtError.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            musicviewmodel.filterTextAll.setValue("");
                        }
                    });

                }else if((networkState.getMsg().contains("no data"))){
                    binding.progressBar.setVisibility(View.GONE);
                    binding.txtError.setVisibility(View.VISIBLE);
                    binding.txtError.setText("No Results found");

                }
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }

}