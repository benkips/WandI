package com.ekarantechnologies.wandi.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ekarantechnologies.wandi.R;
import com.ekarantechnologies.wandi.Utils.NetworkState;
import com.ekarantechnologies.wandi.databinding.ItemBinding;
import com.ekarantechnologies.wandi.databinding.ItemNetworkStateBinding;
import com.ekarantechnologies.wandi.models.Mydatamusicartist;

public class Artistmusicadapter extends PagedListAdapter<Mydatamusicartist, RecyclerView.ViewHolder> {
    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private NetworkState networkState;

    public Artistmusicadapter(Context context) {
        super(Diff_callback);
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_PROGRESS) {
            ItemNetworkStateBinding itemNetworkStateBinding =
                    ItemNetworkStateBinding.inflate(inflater, parent, false);

            return new NetworkStateItemViewHolder(itemNetworkStateBinding);
        } else {
            ItemBinding itemBinding =
                    ItemBinding.inflate(inflater, parent, false);

            return new MusicHolder(itemBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MusicHolder) {
            Mydatamusicartist mydata = getItem(position);
            if (mydata != null) {
                MusicHolder Holder = (MusicHolder) holder;
                Holder.binding.title.setText(mydata.song);
                String streams = (Integer.parseInt(mydata.streams) <= 1) ? " stream" : "streams";
                Holder.binding.subtitle.setText(mydata.streams + streams);
                Holder.binding.ivim.setImageResource(R.drawable.musicicon);
                Holder.binding.size.setVisibility(View.VISIBLE);
                Holder.binding.size.setText(String.valueOf(mydata.size)+" mb");
                Holder.binding.cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("music", mydata.song);
                        bundle.putInt("id",mydata.id);
                        bundle.putString("table","artists");
                        Navigation.findNavController(view).navigate(R.id.action_musicfromartist_to_playmusic, bundle);

                        //((MainActivity) context).mydownload("http://worshipandinstrumentals.ekarantechnologies.com/songs/" +mydata.song);
                        //Navigation
                        //  .createNavigateOnClickListener(R.id.action_dashboard_to_playmusic).onClick(Holder.binding.cv);

                    }
                });
            } else {
                Toast.makeText(context, "data is null", Toast.LENGTH_SHORT).show();
            }

        } else {
            NetworkStateItemViewHolder Holder = (NetworkStateItemViewHolder) holder;

            if (networkState != null && networkState.getStatus() == NetworkState.Status.RUNNING) {
                Holder.binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                Holder.binding.progressBar.setVisibility(View.GONE);
            }

            if (networkState != null && networkState.getStatus() == NetworkState.Status.FAILED) {
                Holder.binding.errorMsg.setVisibility(View.VISIBLE);
                Holder.binding.errorMsg.setText(networkState.getMsg());
            } else {
                Holder.binding.errorMsg.setVisibility(View.GONE);
            }
        }

       /* TypedArray images =  context.getResources().obtainTypedArray(R.array.loading_images);
        int choice = (int) (Math.random() * images.length());
        holder.binding.ivim.setImageResource(images.getResourceId(choice, R.drawable.picone));
        images.recycle();*/
    }

    private boolean hasExtraRow() {
        if (networkState != null && networkState != NetworkState.LOADED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }

    public class MusicHolder extends RecyclerView.ViewHolder {
        ItemBinding binding;

        public MusicHolder(ItemBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }

    public class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {
        ItemNetworkStateBinding binding;

        public NetworkStateItemViewHolder(ItemNetworkStateBinding b) {
            super(b.getRoot());
            binding = b;

        }
    }

    private static DiffUtil.ItemCallback<Mydatamusicartist> Diff_callback = new DiffUtil.ItemCallback<Mydatamusicartist>() {
        @Override
        public boolean areItemsTheSame(@NonNull Mydatamusicartist oldItem, @NonNull Mydatamusicartist newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Mydatamusicartist oldItem, @NonNull Mydatamusicartist newItem) {
            return oldItem.equals(newItem);
        }
    };

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

}

