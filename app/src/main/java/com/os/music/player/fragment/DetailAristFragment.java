package com.os.music.player.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.os.music.player.R;
import com.os.music.player.adapter.MusicAdapter;
import com.os.music.player.database.DBManager;
import com.os.music.player.fragment.basefragment.BaseFragment;
import com.os.music.player.models.MySong;

/**
 * Created by hue on 23/05/2017.
 */

@SuppressLint("ValidFragment")
public class DetailAristFragment extends BaseFragment {
    private TextView tvTitle, tvInfoAlbums;
    private RecyclerView recyclerViewLstAlbums;
    private ArrayList<MySong> listSong = new ArrayList<>();
    private MusicAdapter musicAdapter;
    private Context context;
    private BroadcastReceiver mBroadCastReceive;
    private String name_arist = "";
    @SuppressLint("ValidFragment")
    public DetailAristFragment(String name_arist) {
        this.name_arist = name_arist;
    }

    public String getName_arist() {
        return name_arist;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_albums, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View v) {
        tvTitle = (TextView) v.findViewById(R.id.tvNameAlbumsDetails);
        tvInfoAlbums = (TextView) v.findViewById(R.id.tvInfoAlbums);
        recyclerViewLstAlbums = (RecyclerView) v.findViewById(R.id.rcllstSong);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewLstAlbums.setLayoutManager(mLayoutManager);
        recyclerViewLstAlbums.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLstAlbums.setHasFixedSize(true);
        musicAdapter = new MusicAdapter(getActivity(), listSong,3);
        recyclerViewLstAlbums.setAdapter(musicAdapter);
        name_arist = getName_arist();
        tvTitle.setText(name_arist);
    }

    private void initData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (listSong.size() > 0) {
                    listSong.clear();
                    musicAdapter.notifyDataSetChanged();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                Log.d("getNameArist", name_arist);
                listSong.addAll(DBManager.INSTANCE.getAllSongByArist(name_arist));
                tvInfoAlbums.setText("Có "+ listSong.size()+" bài hát");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                musicAdapter = new MusicAdapter(getActivity(), listSong,3);
                recyclerViewLstAlbums.setAdapter(musicAdapter);
                musicAdapter.notifyDataSetChanged();
            }
        }.execute();

    }
}
