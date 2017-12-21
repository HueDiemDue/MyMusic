package com.os.music.player.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.os.music.player.R;
import com.os.music.player.database.DBManager;
import com.os.music.player.task.GetAllMusic;
import com.os.music.player.utils.Utils;

/**
 * Created by binhnk on 5/22/2017.
 */

public class SplashActivity extends AppCompatActivity {

    private static final String[] PERMISSION = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final int REQUEST_CODE_PERMISSION = 2;

    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Utils.checkPermission(PERMISSION, SplashActivity.this) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(PERMISSION, REQUEST_CODE_PERMISSION);
            } else {
                if(DBManager.INSTANCE.checkDatabaseNull()){
                    new GetAllMusic(this).execute();
                }
                else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }
                    }, 2000);
                }
//                new GetAllMusic(this).execute();

            }
        } else {
            if(DBManager.INSTANCE.checkDatabaseNull()){
                new GetAllMusic(this).execute();
            }
            else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                }, 2000);
            }
//            new GetAllMusic(this).execute();
        }

        IntentFilter filter = new IntentFilter("loading.data");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case "loading.data":
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                finish();
                            }
                        }, 2000);
                        break;

                    default:
                        break;
                }
            }
        };
        registerReceiver(mReceiver, filter);

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (Utils.checkPermission(PERMISSION, SplashActivity.this) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(PERMISSION, REQUEST_CODE_PERMISSION);
                } else {
                    new GetAllMusic(SplashActivity.this).execute();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }
}
