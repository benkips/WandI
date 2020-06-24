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

import com.ekarantechnologies.wandi.R;
import com.ekarantechnologies.wandi.Utils.NetworkState;
import com.ekarantechnologies.wandi.adapters.Artistmusicadapter;
import com.ekarantechnologies.wandi.adapters.Foldermusicadapter;
import com.ekarantechnologies.wandi.databinding.FragmentMusicfromartistBinding;
import com.ekarantechnologies.wandi.databinding.FragmentMusicfromfolderBinding;
import com.ekarantechnologies.wandi.models.Mydatamusicartist;
import com.ekarantechnologies.wandi.models.Mydatamusicfolder;
import com.ekarantechnologies.wandi.viewmodel.Artistmusicviewmodel;
import com.ekarantechnologies.wandi.viewmodel.Foldermusicviewmodel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Musicfromfolder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Musicfromfolder extends Fragment {
    FragmentMusicfromfolderBinding binding;
    Foldermusicviewmodel foldermusicviewmodel;
    Foldermusicadapter foldermusicadapter;
    private int mid;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Musicfromfolder() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Musicfromfolder.
     */
    // TODO: Rename and change types and number of parameters
    public static Musicfromfolder newInstance(String param1, String param2) {
        Musicfromfolder fragment = new Musicfromfolder();
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
        binding= FragmentMusicfromfolderBinding.inflate(getLayoutInflater());
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mid = getArguments().getInt("musicid");
        //Toast.makeText(getContext(), String.valueOf(mid), Toast.LENGTH_SHORT).show();
        foldermusicviewmodel=new ViewModelProvider(this).get(Foldermusicviewmodel.class);
        //recycleview
        binding.rvmusicartist.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.rvmusicartist.setLayoutManager(manager);
        //adapter
        foldermusicadapter=new Foldermusicadapter(getContext());
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
                foldermusicviewmodel.filterTextAll.setValue(query+"^"+mid);

            }
        });
        binding.srchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = binding.srchtxt.getText().toString().trim();
                foldermusicviewmodel.filterTextAll.setValue(query+"^"+mid);
            }
        });
        //begin to searvh
        foldermusicviewmodel.filterTextAll.setValue(""+"^"+mid);
        foldermusicviewmodel.mydatamusicfolderpagedlist.observe(getViewLifecycleOwner(), new Observer<PagedList<Mydatamusicfolder>>() {
            @Override
            public void onChanged(PagedList<Mydatamusicfolder> mydatamusicartists) {

                initialloading();
                loading();

                foldermusicadapter.submitList(mydatamusicartists);
            }
        });
        binding.rvmusicartist.setAdapter(foldermusicadapter);
    }
    private  void loading(){
        foldermusicviewmodel.getNetworkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
                /*Toast.makeText(getContext(), networkState.getMsg(), Toast.LENGTH_SHORT).show();
                Snackbar.make(binding.getRoot(),networkState.getMsg(),Snackbar.LENGTH_LONG).show();*/
                foldermusicadapter.setNetworkState(networkState);


            }
        });
    }
    private  void initialloading(){
        foldermusicviewmodel.getInitialnetworkStateNetworkState().observe(this, new Observer<NetworkState>() {
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
                            foldermusicviewmodel.filterTextAll.setValue(""+"^"+mid);
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