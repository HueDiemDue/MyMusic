package com.os.music.player.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.os.music.player.R;
import com.os.music.player.adapter.MusicAdapter;
import com.os.music.player.database.DBManager;
import com.os.music.player.fragment.basefragment.BaseFragment;
import com.os.music.player.models.MySong;

import java.util.ArrayList;

/**
 * Created by hue on 18/05/2017.
 */

public class AllSongFragment extends BaseFragment implements SearchView.OnQueryTextListener {
    private RecyclerView rclLstSong;
    private ArrayList<MySong> listSong = new ArrayList<>();
    private MusicAdapter musicAdapter;
    private SearchView searchView;
    private BroadcastReceiver broadcastReceiver;
    private LinearLayout lnnListSong;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_list_song, container, false);
        initView(view);
        initData();
        searchView.setOnQueryTextListener(this);
        return view;
    }

    private void initView(View v) {
        rclLstSong = (RecyclerView) v.findViewById(R.id.rclListSong);
        searchView = (SearchView) v.findViewById(R.id.searchVSong);
        lnnListSong = (LinearLayout) v.findViewById(R.id.lnnListSong);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rclLstSong.setLayoutManager(mLayoutManager);
        rclLstSong.setItemAnimator(new DefaultItemAnimator());
        rclLstSong.setHasFixedSize(true);
        musicAdapter = new MusicAdapter(getActivity(), listSong, 0);
        rclLstSong.setAdapter(musicAdapter);
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
                listSong.addAll(DBManager.INSTANCE.getAllSong());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                musicAdapter = new MusicAdapter(getActivity(), listSong, 0);
                rclLstSong.setAdapter(musicAdapter);
                musicAdapter.notifyDataSetChanged();
            }
        }.execute();

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    // phuong thuc loc khi search
    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            musicAdapter.getFilter().filter("");
            rclLstSong.clearFocus();
        } else {
            musicAdapter.getFilter().filter(newText.toString());
        }
        return false;
    }

    @Override
    public void onDestroy() {
        if (broadcastReceiver != null) {
            context.unregisterReceiver(broadcastReceiver);
        }
        super.onDestroy();
    }


}
