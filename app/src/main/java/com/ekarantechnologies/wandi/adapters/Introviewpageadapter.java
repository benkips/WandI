package com.ekarantechnologies.wandi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.ekarantechnologies.wandi.R;
import com.ekarantechnologies.wandi.models.ScreenItem;


import java.util.List;

public class Introviewpageadapter extends PagerAdapter {

    Context context;
    List<ScreenItem> mlistsren;

    public Introviewpageadapter(Context context, List<ScreenItem> mlistsren) {
        this.context = context;
        this.mlistsren = mlistsren;
    }

    @Override
    public int getCount() {
        return mlistsren.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.layout_screen,null);
        LottieAnimationView lottieAnimationView=view.findViewById(R.id.lottieAnimationView);
        TextView title=view.findViewById(R.id.introtitle);
        TextView description=view.findViewById(R.id.introdesc);
        lottieAnimationView.setImageAssetsFolder("assets");

        title.setText(mlistsren.get(position).getTitle());
        description.setText(mlistsren.get(position).getDescription());
        lottieAnimationView.setAnimation(mlistsren.get(position).getImage());

        container.addView(view);
        return  view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
