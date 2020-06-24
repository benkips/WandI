package com.ekarantechnologies.wandi.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ekarantechnologies.wandi.BuildConfig;
import com.ekarantechnologies.wandi.Listeners.ActionListener;
import com.ekarantechnologies.wandi.MainActivity;
import com.ekarantechnologies.wandi.R;
import com.ekarantechnologies.wandi.Utils.Utils;
import com.ekarantechnologies.wandi.databinding.DownloadItemBinding;
import com.ekarantechnologies.wandi.models.DownloadData;
import com.ekarantechnologies.wandi.models.Myaudio;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Status;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class FileAdapter extends ListAdapter<DownloadData, RecyclerView.ViewHolder> {


    @NonNull
    private final List<DownloadData> downloads = new ArrayList<>();
    @NonNull
    private final ActionListener actionListener;

 /*   public FileAdapter(@NonNull final ActionListener actionListener) {
        this.actionListener = actionListener;
    }*/

    public FileAdapter(ActionListener actionListener) {
        super(diffCallback);
        this.actionListener = actionListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DownloadItemBinding downloadItemBinding = DownloadItemBinding.inflate(inflater, parent, false);
        return new ViewHolder(downloadItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder Holder = (ViewHolder) holder;
        Holder.binding.actionButton.setOnClickListener(null);
        Holder.binding.actionButton.setEnabled(true);

        final DownloadData downloadData = downloads.get(position);
        String url = "";
        if (downloadData.download != null) {
            url = downloadData.download.getUrl();
        }
        final Uri uri = Uri.parse(url);
        final Status status = downloadData.download.getStatus();
        final Context context = Holder.itemView.getContext();

        Holder.binding.titleTextView.setText(uri.getLastPathSegment());
        Holder.binding.statusTextView.setText(getStatusString(status));

        int progress = downloadData.download.getProgress();
        if (progress == -1) { // Download progress is undermined at the moment.
            progress = 0;
        }
        Holder.binding.progressBar.setProgress(progress);
        Holder.binding.progressTextView.setText(context.getString(R.string.percent_progress, progress));

        if (downloadData.eta == -1) {
            Holder.binding.remainingTextView.setText("");
        } else {
            Holder.binding.remainingTextView.setText(Utils.getETAString(context, downloadData.eta));
        }

        if (downloadData.downloadedBytesPerSecond == 0) {
            Holder.binding.downloadSpeedTextView.setText("");
        } else {
            Holder.binding.downloadSpeedTextView.setText(Utils.getDownloadSpeedString(context, downloadData.downloadedBytesPerSecond));
        }

        switch (status) {
            case COMPLETED: {
                Holder.binding.actionButton.setText("completed");
                Holder.binding.actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            Toast.makeText(context, "Downloaded Path:" + String.valueOf(downloadData.download.getFile()), LENGTH_LONG).show();
                            return;
                        }
                        final File file = new File(downloadData.download.getFile());
                        final Uri uri1 = Uri.fromFile(file);
                        final Intent share = new Intent(Intent.ACTION_VIEW);
                        share.setDataAndType(uri1, Utils.getMimeType(context, uri1));
                        context.startActivity(share);


                    }
                });

                break;
            }
            case FAILED: {
                Holder.binding.actionButton.setText("Retry");
                Holder.binding.actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Holder.binding.actionButton.setEnabled(false);
                        actionListener.onRetryDownload(downloadData.download.getId());
                    }
                });
                break;
            }
            case PAUSED: {
                Holder.binding.actionButton.setText("Resume");
                Holder.binding.actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Holder.binding.actionButton.setEnabled(false);
                        actionListener.onResumeDownload(downloadData.download.getId());
                    }
                });
                break;
            }
            case DOWNLOADING:
            case QUEUED: {
                Holder.binding.actionButton.setText("Pause");
                Holder.binding.actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Holder.binding.actionButton.setEnabled(false);
                        actionListener.onPauseDownload(downloadData.download.getId());
                    }
                });
                break;
            }
            case ADDED: {
                Holder.binding.actionButton.setText("download");
                Holder.binding.actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Holder.binding.actionButton.setEnabled(false);
                        actionListener.onResumeDownload(downloadData.download.getId());
                    }
                });
                break;
            }
            default: {
                break;
            }
        }

        //Set delete action
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Uri uri12 = Uri.parse(downloadData.download.getUrl());
                new AlertDialog.Builder(context)
                        .setMessage(context.getString(R.string.delete_title, uri12.getLastPathSegment()))
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                actionListener.onRemoveDownload(downloadData.download.getId());
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

                return true;
            }
        });

    }

    public void addDownload(@NonNull final Download download) {
        boolean found = false;
        DownloadData data = null;
        int dataPosition = -1;
        for (int i = 0; i < downloads.size(); i++) {
            final DownloadData downloadData = downloads.get(i);
            if (downloadData.id == download.getId()) {
                data = downloadData;
                dataPosition = i;
                found = true;
                break;
            }
        }
        if (!found) {
            final DownloadData downloadData = new DownloadData();
            downloadData.id = download.getId();
            downloadData.download = download;
            downloads.add(downloadData);
            notifyItemInserted(downloads.size() - 1);
        } else {
            data.download = download;
            notifyItemChanged(dataPosition);
        }
    }

    @Override
    public int getItemCount() {
        return downloads.size();
    }

    public void update(@NonNull final Download download, long eta, long downloadedBytesPerSecond) {
        for (int position = 0; position < downloads.size(); position++) {
            final DownloadData downloadData = downloads.get(position);
            if (downloadData.id == download.getId()) {
                switch (download.getStatus()) {
                    case REMOVED:
                    case DELETED: {
                        downloads.remove(position);
                        notifyItemRemoved(position);
                        break;
                    }
                    default: {
                        downloadData.download = download;
                        downloadData.eta = eta;
                        downloadData.downloadedBytesPerSecond = downloadedBytesPerSecond;
                        notifyItemChanged(position);
                    }
                }
                return;
            }
        }
    }

    private String getStatusString(Status status) {
        switch (status) {
            case COMPLETED:
                return "Done";
            case DOWNLOADING:
                return "Downloading";
            case FAILED:
                return "Error";
            case PAUSED:
                return "Paused";
            case QUEUED:
                return "Waiting in Queue";
            case REMOVED:
                return "Removed";
            case NONE:
                return "Not Queued";
            default:
                return "Unknown";
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        DownloadItemBinding binding;

        ViewHolder(DownloadItemBinding b) {
            super(b.getRoot());
            binding = b;
        }

    }

    private static DiffUtil.ItemCallback<DownloadData> diffCallback = new DiffUtil.ItemCallback<DownloadData>() {
        @Override
        public boolean areItemsTheSame(@NonNull DownloadData oldItem, @NonNull DownloadData newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull DownloadData oldItem, @NonNull DownloadData newItem) {
            return oldItem.equals(newItem);
        }
    };


}
