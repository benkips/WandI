package com.ekarantechnologies.wandi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ekarantechnologies.wandi.adapters.Introviewpageadapter;
import com.ekarantechnologies.wandi.databinding.ActivityOnboardBinding;
import com.ekarantechnologies.wandi.models.ScreenItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Onboard extends AppCompatActivity {
    private Introviewpageadapter introviewpageadapter;
    final String Tag=this.getClass().getName();
    int position = 0;
    ActivityOnboardBinding binding;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_onboard);
        binding=ActivityOnboardBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        storageperm();
        preferences=getSharedPreferences("logininfo.conf",MODE_PRIVATE);
        String opener=preferences.getString("keyx","");


        if(!opener.equals("")){
            /*startActivity here*/
            Log.d(Tag,opener);
            startActivity(new Intent(Onboard.this,MainActivity.class));
            Onboard.this.finish();
        }



        final List<ScreenItem> mlist = new ArrayList<>();
        mlist.add(new ScreenItem("Worship  And \n Instrumentals", "Get the best worship music from all around world", "violin.json"));
        mlist.add(new ScreenItem("Listen offline", "Download music online and listen offline", "music.json"));
        mlist.add(new ScreenItem("Stream And Share", "Share the music to all platforms", "guitar.json"));

        introviewpageadapter = new Introviewpageadapter(Onboard.this, mlist);
        binding.screenViewpager.setAdapter(introviewpageadapter);
        binding.tabIndicato.setupWithViewPager(binding.screenViewpager);

        binding.nxtbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = binding.screenViewpager.getCurrentItem();
                if (position < mlist.size()) {
                    position++;
                    binding.screenViewpager.setCurrentItem(position);
                }
                if (position == mlist.size()-1) {
                    loadscreen();
                }
            }
        });
        binding.gtstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingstarted();
            }
        });
        binding.tabIndicato.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mlist.size()- 1) {
                    loadscreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void loadscreen() {
        binding.nxtbutton.setVisibility(View.INVISIBLE);
        binding.tabIndicato.setVisibility(View.INVISIBLE);
        binding.gtstarted.setVisibility(View.VISIBLE);
    }
    private  void gettingstarted(){

        SharedPreferences.Editor editor = preferences.edit();
        String keys="hello";
        editor.putString("keyx",keys);
        editor.apply();
        Log.d(Tag,"saved");
        startActivity(new Intent(Onboard.this,MainActivity.class));
        Onboard.this.finish();
    }
    private void storageperm() {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int grant = ContextCompat.checkSelfPermission(Onboard.this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(Onboard.this, permission_list, 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    storageperm();
                    Toast.makeText(Onboard.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
