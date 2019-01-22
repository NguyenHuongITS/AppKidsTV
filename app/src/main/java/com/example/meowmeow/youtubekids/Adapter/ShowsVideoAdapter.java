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

import com.example.meowmeow.youtubekids.Interface.ShowsVideo;
import com.example.meowmeow.youtubekids.Model.PlayVideoYTB;
import com.example.meowmeow.youtubekids.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ShowsVideoAdapter extends RecyclerView.Adapter<ShowsVideoAdapter.MyViewHolder> {
    private Context context;
    private int layout;
    private List<ShowsVideo> showsVideoList;
    private LayoutInflater mlayoutInflater;

    public ShowsVideoAdapter(Context context, int layout, List<ShowsVideo> showsVideoList) {
        this.context = context;
        this.layout = layout;
        this.showsVideoList = showsVideoList;
        mlayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = mlayoutInflater.inflate(R.layout.item_custom_video,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ShowsVideo showsVideo = showsVideoList.get(position);
        holder.textView.setText(showsVideo.getTitle());
        Picasso.with(context).load(showsVideo.getThumbnails()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return showsVideoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.txt_musicvideo);
            imageView = (ImageView) itemView.findViewById(R.id.img_musicvideo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowsVideo showsVideo = showsVideoList.get(getAdapterPosition());
                    String videoId = showsVideo.getIdVideo();
                    Intent intent = new Intent(context, PlayVideoYTB.class);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("videoId",videoId);
                    context.startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(context, "Long item click", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }
}
