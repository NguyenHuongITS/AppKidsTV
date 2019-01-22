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

import com.example.meowmeow.youtubekids.Interface.ExploreVideo;
import com.example.meowmeow.youtubekids.Interface.LearningVideo;
import com.example.meowmeow.youtubekids.Model.PlayVideoYTB;
import com.example.meowmeow.youtubekids.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LearningVideoAdapter extends RecyclerView.Adapter<LearningVideoAdapter.MyViewHolder> {
    private Context context;
    private int layout;
    private LayoutInflater mlayoutInflater;
    private List<LearningVideo> learningVideoList;

    public LearningVideoAdapter(Context context, int layout, List<LearningVideo> learningVideoList)
    {
        this.context = context;
        this.layout = layout;
        this.learningVideoList = learningVideoList;
        mlayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = mlayoutInflater.inflate(R.layout.item_custom_video,parent,false);
        return new LearningVideoAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LearningVideo learningVideo = learningVideoList.get(position);
        holder.textView.setText(learningVideo.getTitle());
        Picasso.with(context).load(learningVideo.getThumbnails()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return learningVideoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;

        public MyViewHolder(final View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.txt_musicvideo);
            imageView = (ImageView) itemView.findViewById(R.id.img_musicvideo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LearningVideo learningVideo = learningVideoList.get(getAdapterPosition());
                    String videoId = learningVideo.getIdVideo();
                    Intent intent = new Intent(context, PlayVideoYTB.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("videoId",videoId);
                    context.startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(context,"Long item clicked " + textView.getText() ,Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }
}
