package com.os.music.player.fragment;

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
import com.os.music.player.adapter.AristAdapter;
import com.os.music.player.database.DBManager;
import com.os.music.player.fragment.basefragment.BaseFragment;

/**
 * Created by hue on 18/05/2017.
 */


public class AristFragment extends BaseFragment {
    private Context context;
    private RecyclerView rclArist;
    private AristAdapter customAdapter;
    private ArrayList<String> listArist = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_arist, container, false);
        initView(view);
        initData();
        return view;
    }
    private void initView(View v){
        rclArist = (RecyclerView) v.findViewById(R.id.rcllstArist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rclArist.setLayoutManager(mLayoutManager);
        rclArist.setItemAnimator(new DefaultItemAnimator());
        rclArist.setHasFixedSize(true);
        customAdapter = new AristAdapter(context, listArist);
        rclArist.setAdapter(customAdapter);
    }
    private void initData(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (listArist.size() > 0) {
                    listArist.clear();
                    customAdapter.notifyDataSetChanged();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                listArist.addAll(DBManager.INSTANCE.getListArist());
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
