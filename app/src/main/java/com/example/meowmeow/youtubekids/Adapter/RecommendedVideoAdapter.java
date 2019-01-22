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

import com.example.meowmeow.youtubekids.Interface.MusicVideo;
import com.example.meowmeow.youtubekids.Interface.RecommendedVideo;
import com.example.meowmeow.youtubekids.Model.PlayVideoYTB;
import com.example.meowmeow.youtubekids.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class RecommendedVideoAdapter extends RecyclerView.Adapter<RecommendedVideoAdapter.MyViewHolder> {
    private Context context;
    int layout;
    private LayoutInflater mLayoutInflater;
    private List<RecommendedVideo> recommendedVideoList;


    public RecommendedVideoAdapter(Context context, int layout, List<RecommendedVideo> recommendedVideoList) {
        this.context = context;
        this.layout = layout;
        this.recommendedVideoList = recommendedVideoList;
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
                    RecommendedVideo recommendedVideo = recommendedVideoList.get(getAdapterPosition());
                    String videoId = recommendedVideo.getIdVideo();
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
    public RecommendedVideoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(R.layout.item_custom_video, parent, false);
        return new RecommendedVideoAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(RecommendedVideoAdapter.MyViewHolder holder, int position) {
        RecommendedVideo recommendedVideo = recommendedVideoList.get(position);
        holder.txtTitle.setText(recommendedVideo.getTitle());
        Picasso.with(context).load(recommendedVideo.getThumbnails()).into(holder.imgThumbnails);
    }

    @Override
    public int getItemCount() {
        return recommendedVideoList.size();
    }
}
