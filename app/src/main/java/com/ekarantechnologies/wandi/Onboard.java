package com.ekarantechnologies.wandi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

}
