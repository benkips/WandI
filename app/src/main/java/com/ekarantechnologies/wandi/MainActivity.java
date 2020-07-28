package com.ekarantechnologies.wandi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.Notification;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import com.ekarantechnologies.wandi.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DownloadBlock;
import com.tonyodev.fetch2core.Func;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

import static com.ekarantechnologies.wandi.App.CHANNEL_1_ID;

public class MainActivity extends AppCompatActivity implements FetchListener {
    ActivityMainBinding binding;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_WRITING_PERMISSION = 200;
    private static String fileName = null;
    private Fetch fetch;
    final String Tag = this.getClass().getName();
    private NotificationManagerCompat notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove api incopatibility warning
        notificationManager=NotificationManagerCompat.from(MainActivity.this);        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectNonSdkApiUsage()
                    .penaltyLog()
                    .build());
        }

        ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_WRITING_PERMISSION);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//       setContentView(R.layout.activity_main);
//        BottomNavigationView bottomNavigationView=findViewById(R.id.Botomnavigationview);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(binding.Botomnavigationview, navController);
    }





    @Override
    public void onAdded(@NotNull Download download) {
        shownotification(download.getId());
    }

    @Override
    public void onCancelled(@NotNull Download download) {

    }

    @Override
    public void onCompleted(@NotNull Download download) {
        Toast.makeText(getApplicationContext(), "Download complete", Toast.LENGTH_SHORT).show();

        Bitmap myicon= BitmapFactory.decodeResource(getResources(),R.drawable.headphones);
        Notification notification=new NotificationCompat.Builder(MainActivity.this,CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_arrow_downward_24)
                .setContentText("Download complete")
                .setProgress(0, 0, false)
                .setAutoCancel(false)
                .setLargeIcon(myicon)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        notificationManager.notify(download.getId(),notification);
    }

    @Override
    public void onDeleted(@NotNull Download download) {

    }

    @Override
    public void onDownloadBlockUpdated(@NotNull Download download, @NotNull DownloadBlock downloadBlock, int i) {

    }

    @Override
    public void onError(@NotNull Download download, @NotNull Error error, @Nullable Throwable throwable) {

    }

    @Override
    public void onPaused(@NotNull Download download) {

    }

    @Override
    public void onProgress(@NotNull Download download, long l, long l1) {
        int progress = download.getProgress();
        //Toast.makeText(this, String.valueOf(progress), Toast.LENGTH_SHORT).show();

        Bitmap myicon= BitmapFactory.decodeResource(getResources(),R.drawable.headphones);
        Notification notification=new NotificationCompat.Builder(MainActivity.this,CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_arrow_downward_24)
                .setContentText("Downloading..:"+String.valueOf(progress)+"%")
                .setProgress(100, progress, false)
                .setAutoCancel(false)
                .setLargeIcon(myicon)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        notificationManager.notify(download.getId(),notification);
    }

    @Override
    public void onQueued(@NotNull Download download, boolean b) {

    }

    @Override
    public void onRemoved(@NotNull Download download) {

    }

    @Override
    public void onResumed(@NotNull Download download) {

    }

    @Override
    public void onStarted(@NotNull Download download, @NotNull List<? extends DownloadBlock> list, int i) {

    }

    @Override
    public void onWaitingNetwork(@NotNull Download download) {

    }
    public void mydownload(String url) {

        String f = getNameFromUrl(url);
        String fileName = Environment.getExternalStorageDirectory() + File.separator + "worship/";
        //Create  folder if it does not exist
        File exportDir = new File(fileName);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        String[] separated = f.split("\\.");
        String nm = separated[0];
        String ext = separated[1];

        File file = new File(fileName, nm + "." + ext);
        String fullpath = file.getAbsolutePath();

        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this)
                .setDownloadConcurrentLimit(3)
                .build();

        final Request request = new Request(url, fullpath);
        request.setPriority(Priority.HIGH);
        request.setNetworkType(NetworkType.ALL);

        fetch = Fetch.Impl.getInstance(fetchConfiguration);
        fetch.enqueue(request, new Func<Request>() {
            @Override
            public void call(@NotNull Request result) {
                Toast.makeText(MainActivity.this, "Download has started", Toast.LENGTH_SHORT).show();
            }
        }, new Func<Error>() {
            @Override
            public void call(@NotNull Error result) {
                Toast.makeText(MainActivity.this, "Error:" + result.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        fetch.addListener(this);
    }

    private String getNameFromUrl(final String url) {
        return Uri.parse(url).getLastPathSegment();
    }

    private void shownotification(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            Bitmap myicon= BitmapFactory.decodeResource(getResources(),R.drawable.headphones);
            Notification notification=new NotificationCompat.Builder(MainActivity.this,CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_baseline_arrow_downward_24)
                    .setContentTitle("Downloading File")
                    .setProgress(100, 0, false)
                    .setAutoCancel(false)
                    .setLargeIcon(myicon)
                    .setWhen(System.currentTimeMillis())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build();

            notificationManager.notify(id,notification);
        }
    }



}