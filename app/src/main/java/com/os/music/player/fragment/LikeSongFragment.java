package com.os.music.player.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.os.music.player.R;
import com.os.music.player.adapter.MusicAdapter;
import com.os.music.player.database.DBManager;
import com.os.music.player.fragment.basefragment.BaseFragment;
import com.os.music.player.models.MySong;
import com.os.music.player.utils.Constants;

/**
 * Created by hue on 18/05/2017.
 */

public class LikeSongFragment extends BaseFragment {
    private RecyclerView rclLstLikeSong;
    private ArrayList<MySong> listSong = new ArrayList<>();
    private MusicAdapter likedAdapter;
    private Context context;

    private BroadcastReceiver mReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_like_song, container, false);
        initView(view);
        initData();
        IntentFilter filter = new IntentFilter(Constants.Intents.UPDATE_LIKED);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.Intents.UPDATE_LIKED)) {
                    initData();
                }
            }
        };
        context.registerReceiver(mReceiver, filter);
        return view;
    }

    private void initView(View v) {
        rclLstLikeSong = (RecyclerView) v.findViewById(R.id.rclLikeSong);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rclLstLikeSong.setLayoutManager(mLayoutManager);
        rclLstLikeSong.setItemAnimator(new DefaultItemAnimator());
        rclLstLikeSong.setHasFixedSize(true);
        likedAdapter = new MusicAdapter(context, listSong,4);
        rclLstLikeSong.setAdapter(likedAdapter);
    }

    private void initData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                if (listSong.size() > 0) {
                    listSong.clear();
                    likedAdapter.notifyDataSetChanged();
                }
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                listSong.addAll(DBManager.INSTANCE.getAllSongByLike());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
//                likedAdapter.notifyDataSetChanged();
                super.onPostExecute(aVoid);
                likedAdapter = new MusicAdapter(getActivity(), listSong,4);
                rclLstLikeSong.setAdapter(likedAdapter);
                likedAdapter.notifyDataSetChanged();
            }
        }.execute();
    }


    @Override
    public void onDestroy() {
        if (mReceiver != null) {
            context.unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }
}
