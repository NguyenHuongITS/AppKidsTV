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

import com.example.meowmeow.youtubekids.Interface.PlayVideo;
import com.example.meowmeow.youtubekids.Model.PlayVideoYTB;
import com.example.meowmeow.youtubekids.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class PlayVideoAdapter extends RecyclerView.Adapter<PlayVideoAdapter.MyViewHolder> {
    Context context;
    int layout;
    LayoutInflater layoutInflater;
    List<PlayVideo> playVideoList;

    public PlayVideoAdapter(Context context, int layout, List<PlayVideo> playVideoList) {
        this.context = context;
        this.layout = layout;
        this.playVideoList = playVideoList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public PlayVideoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = layoutInflater.inflate(R.layout.item_play_video_ytb, parent, false);
        return new PlayVideoAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(PlayVideoAdapter.MyViewHolder holder, int position) {
        PlayVideo playVideo = playVideoList.get(position);
        holder.txtTitle.setText(playVideo.getTitle());
        Picasso.with(context).load(playVideo.getThumbnails()).into(holder.imgThumbnails);
    }

    @Override
    public int getItemCount() {
        return playVideoList.size();
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
                public void onClick(View v) {
                    PlayVideo playVideo = playVideoList.get(getAdapterPosition());
                    String videoId = playVideo.getIdVideo();
                    Intent intent = new Intent(context, PlayVideoYTB.class);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("videoId", videoId);
                    context.startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, "Long item clicked " + txtTitle.getText(), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }
}
