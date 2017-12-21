package com.os.music.player.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.os.music.player.R;
import com.os.music.player.models.Albums;
import com.os.music.player.utils.Constants;

/**
 * Created by hue on 19/05/2017.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Albums> lstMusic = new ArrayList<>();
    private LayoutInflater inflater;

    public AlbumsAdapter(Context context, ArrayList<Albums> lst) {
        this.context = context;
        this.lstMusic = lst;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_albums, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {
        final Albums albums = lstMusic.get(position);
        holder.tvName.setText(albums.getNameAlbums());
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), Integer.parseInt(lstMusic.get(position).getId()));
//        holder.imageView.setImageBitmap(bitmap);
//        holder.imageView.setImageResource( Integer.parseInt(lstMusic.get(position).getId()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //gui den activity => switch fragment chua list bai hat o album do ( gui toi main)
                Intent activityIntent = new Intent(Constants.Intents.SEND_FROM_ALBUMS_ADAPTER_TO_MAIN);
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityIntent.putExtra(Constants.Intents.NAME_ALBUMS, albums.getNameAlbums());
                Log.d("AlbumsAdapter",albums.getNameAlbums());
                context.sendBroadcast(activityIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return lstMusic.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            imageView = (ImageView) itemView.findViewById(R.id.img_avt_albums);

        }
    }
}
