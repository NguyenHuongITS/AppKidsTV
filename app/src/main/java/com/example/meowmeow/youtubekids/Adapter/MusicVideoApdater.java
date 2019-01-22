package com.example.meowmeow.youtubekids.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meowmeow.youtubekids.Model.PlayVideoYTB;
import com.example.meowmeow.youtubekids.R;

import com.example.meowmeow.youtubekids.Interface.MusicVideo;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MusicVideoApdater extends RecyclerView.Adapter<MusicVideoApdater.MyViewHolder> {
    private Context context;
    private int layout;
    private LayoutInflater mLayoutInflater;
    private List<MusicVideo> musicVideoList;


    public MusicVideoApdater(Context context, int layout, List<MusicVideo> musicVideoList) {
        this.context = context;
        this.layout = layout;
        this.musicVideoList = musicVideoList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgThumbnails;
        public TextView txtTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgThumbnails = (ImageView) itemView.findViewById(R.id.img_musicvideo);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_musicvideo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MusicVideo musicVideo = musicVideoList.get(getAdapterPosition());
                    String videoId = musicVideo.getIdVideo();
                    Intent intent = new Intent(context, PlayVideoYTB.class);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("videoId", videoId);
                    context.startActivity(intent);
                    //Toast.makeText(context,txtTitle.getText(), Toast.LENGTH_SHORT).show();
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(context, "Long item clicked " + txtTitle.getText(), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(R.layout.item_custom_video, parent, false);
        return new MusicVideoApdater.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MusicVideo musicVideo = musicVideoList.get(position);
        holder.txtTitle.setText(musicVideo.getTitle());
        Picasso.with(context).load(musicVideo.getThumbnails()).into(holder.imgThumbnails);
    }

    @Override
    public int getItemCount() {
        return musicVideoList.size();
    }
}
