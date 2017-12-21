package com.os.music.player.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import com.os.music.player.adapter.AlbumsAdapter;
import com.os.music.player.database.DBManager;
import com.os.music.player.fragment.basefragment.BaseFragment;
import com.os.music.player.models.Albums;

/**
 * Created by hue on 18/05/2017.
 */

public class AlbumFragment extends BaseFragment {
    private RecyclerView rclLstAlbums;
    private ArrayList<Albums> listSong = new ArrayList<>();
    private AlbumsAdapter customAdapter;
    private BroadcastReceiver broadcastReceiver;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_albums, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View v) {
        rclLstAlbums = (RecyclerView) v.findViewById(R.id.rcllstArist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rclLstAlbums.setLayoutManager(mLayoutManager);
        rclLstAlbums.setItemAnimator(new DefaultItemAnimator());
        rclLstAlbums.setHasFixedSize(true);
        customAdapter = new AlbumsAdapter(context, listSong);
        rclLstAlbums.setAdapter(customAdapter);
    }

    private void initData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (listSong.size() > 0) {
                    listSong.clear();
                    customAdapter.notifyDataSetChanged();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                listSong.addAll(DBManager.INSTANCE.getListAlbums());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                customAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
