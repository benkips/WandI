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

import com.bumptech.glide.Glide;
import com.ekarantechnologies.wandi.R;
import com.ekarantechnologies.wandi.Utils.NetworkState;

import com.ekarantechnologies.wandi.databinding.ItemBinding;
import com.ekarantechnologies.wandi.databinding.ItemNetworkStateBinding;
import com.ekarantechnologies.wandi.models.Mydataartist;

public class Artistadpter extends PagedListAdapter<Mydataartist, RecyclerView.ViewHolder> {
    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private NetworkState networkState;

    public Artistadpter(Context context) {
        super(Diff_callback);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        if(viewType == TYPE_PROGRESS) {
            ItemNetworkStateBinding itemNetworkStateBinding =
                    ItemNetworkStateBinding.inflate(inflater, parent, false);

            return new NetworkStateItemViewHolder(itemNetworkStateBinding);
        }else{
            ItemBinding itemBinding =
                    ItemBinding.inflate(inflater, parent, false);

            return new Artistholder(itemBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof Artistholder) {
            Mydataartist mydata=getItem(position);
            if(mydata!=null){
                Artistholder Holder = ( Artistholder) holder;
                Holder.binding.title.setText(mydata.artist);
                String songs=( Integer.parseInt(mydata.total)<=1)? " song" :" songs";
                Holder.binding.subtitle.setText(mydata.total +songs);
                Glide.with(Holder.binding.ivim)
                        .load(mydata.photo)
                        .into(Holder.binding.ivim);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("musicid",mydata.id);
                        Navigation.findNavController(view).navigate(R.id.action_dashboard_to_musicfromartist,bundle);
                    }
                });
            }else{
                Toast.makeText(context, "data is null", Toast.LENGTH_SHORT).show();
            }

        }else {
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
    public class Artistholder extends RecyclerView.ViewHolder  {
        ItemBinding binding;
        public Artistholder(ItemBinding b) {
            super( b.getRoot());
            binding=b;
        }
    }
    public class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {
        ItemNetworkStateBinding binding;
        public NetworkStateItemViewHolder(ItemNetworkStateBinding b) {
            super(b.getRoot());
            binding=b;

        }
    }
    private  static DiffUtil.ItemCallback<Mydataartist> Diff_callback=new DiffUtil.ItemCallback<Mydataartist>() {
        @Override
        public boolean areItemsTheSame(@NonNull Mydataartist oldItem, @NonNull Mydataartist newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Mydataartist oldItem, @NonNull Mydataartist newItem) {
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
