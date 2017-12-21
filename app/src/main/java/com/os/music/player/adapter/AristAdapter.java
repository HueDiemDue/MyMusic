package com.os.music.player.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.os.music.player.R;
import com.os.music.player.utils.Constants;

/**
 * Created by hue on 19/05/2017.
 */

public class AristAdapter extends RecyclerView.Adapter<AristAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> lstArist = new ArrayList<>();

    public AristAdapter(Context context, ArrayList<String> lst) {
        this.context = context;
        this.lstArist = lst;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_arist, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String name_arist = lstArist.get(position);
        holder.tvName.setText(name_arist);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityIntent = new Intent(Constants.Intents.SEND_FROM_ARIST_ADAPTER_TO_MAIN);
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityIntent.putExtra(Constants.Intents.NAME_ARIST, lstArist.get(position));

                context.sendBroadcast(activityIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstArist.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvNameArist);

        }
    }
}
