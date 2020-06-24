package com.ekarantechnologies.wandi.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ekarantechnologies.wandi.fragments.Taskz;
import com.ekarantechnologies.wandi.fragments.filez;
import com.ekarantechnologies.wandi.subfragments.Artist;
import com.ekarantechnologies.wandi.subfragments.Folderz;
import com.ekarantechnologies.wandi.subfragments.Music;

public class vpageradapter extends FragmentStateAdapter {

    public vpageradapter(@NonNull Fragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new Music();
            case 1:
                return new Artist();
            default:
                return  new Folderz();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
