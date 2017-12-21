package com.os.music.player.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import com.os.music.player.R;
import com.os.music.player.activity.PlayMusicActivity;
import com.os.music.player.models.MySong;
import com.os.music.player.service.MusicService;
import com.os.music.player.shareprefrences.PreferenceUtils;
import com.os.music.player.utils.Constants;

/**
 * Created by hue on 18/05/2017.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private ArrayList<MySong> lstMusic = new ArrayList<>();
    private ArrayList<MySong> lstBackup = new ArrayList<>();
    private int FRAGMENT_TYPE;
    public MusicAdapter(Context context, ArrayList<MySong> lst,int fragmentPosition) {
        this.context = context;
        this.lstMusic.addAll(lst);
        lstBackup.addAll(lst);
        this.FRAGMENT_TYPE = fragmentPosition;
    }


    @Override
    public MusicAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        MySong mSong = lstMusic.get(position);
        holder.tvNameSong.setText(mSong.getNameSong());
        holder.tvNameArist.setText(mSong.getNameArist());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send play music activity
                Intent activityIntent = new Intent(context, PlayMusicActivity.class);
                activityIntent.putExtra(Constants.Intents.ID, lstMusic.get(position).getId());
                context.startActivity(activityIntent);

                // send data to services => play music
                Intent intentService = new Intent(context, MusicService.class);
                String share_song_id = PreferenceUtils.getStringFromPreference(Constants.Intents.ID, context);
                if (share_song_id.equals(lstMusic.get(position).getId())) {
                  intentService.putExtra(Constants.Intents.CURRENT_POSITION,PreferenceUtils.getIntFromPreference(Constants.Intents.CURRENT_POSITION,context));
                }
                else {
                   intentService.putExtra(Constants.Intents.CURRENT_POSITION,0);
                }
                switch (FRAGMENT_TYPE){
                    case 0 :
                        // all song
                        intentService.putExtra(Constants.Intents.FRAGMENT_TYPE,0);
                        break;
                    case 1 :
                        // albums song
                        intentService.putExtra(Constants.Intents.FRAGMENT_TYPE,1);

                        break;
                    case 3:
                        // artist song
                        intentService.putExtra(Constants.Intents.FRAGMENT_TYPE,3);
                        break;
                    case 4 :
                        // like song
                        intentService.putExtra(Constants.Intents.FRAGMENT_TYPE,4);
                        break;
                    default:
                        break;
                }
                intentService.putExtra(Constants.Intents.ID, lstMusic.get(position).getId());

                context.startService(intentService);
            }
        });

    }

    @Override
    public int getItemCount() {
        return lstMusic.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = lstBackup;
                    results.count = lstBackup.size();
                } else {
                    ArrayList<MySong> NewData = new ArrayList<>();
                    for (MySong c : lstBackup)
                        if (c.getNameSong().toLowerCase().contains(constraint.toString().toLowerCase()) || c.getNameArist().toLowerCase().contains(constraint.toString().toLowerCase()))
                            NewData.add(c);
                    results.values = NewData;
                    results.count = NewData.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                lstMusic.clear();
                if (TextUtils.isEmpty(constraint)) {

                    lstMusic.addAll(lstBackup);
                } else {
                    for (MySong song : (ArrayList<MySong>) results.values)
                        lstMusic.add(song);
                }
                notifyDataSetChanged();

            }
        };
        return filter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameSong, tvNameArist;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvNameSong = (TextView) itemView.findViewById(R.id.tvNameSong);
            tvNameArist = (TextView) itemView.findViewById(R.id.tvNameArist);

        }
    }
}
