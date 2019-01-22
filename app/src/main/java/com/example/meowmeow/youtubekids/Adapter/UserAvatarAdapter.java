package com.example.meowmeow.youtubekids.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.meowmeow.youtubekids.Interface.ShowsVideo;
import com.example.meowmeow.youtubekids.Interface.UserAvatar;
import com.example.meowmeow.youtubekids.Model.EditUserActivity;
import com.example.meowmeow.youtubekids.R;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class UserAvatarAdapter extends RecyclerView.Adapter<UserAvatarAdapter.MyViewHolder> {
    private List<UserAvatar> userAvatarList;
    private LayoutInflater layoutInflater;
    private int layout;
    private Context context;

    public UserAvatarAdapter(Context context, int layout, List<UserAvatar> userAvatars) {
        this.context = context;
        this.layout = layout;
        this.userAvatarList = userAvatars;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = layoutInflater.inflate(R.layout.item_avatar_user,parent,false);
        return new UserAvatarAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserAvatar userAvatar = userAvatarList.get(position);
        Picasso.with(context).load(userAvatar.getAvatar()).into(holder.circularImageView);
    }

    @Override
    public int getItemCount() {
        return userAvatarList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircularImageView circularImageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            circularImageView = (CircularImageView) itemView.findViewById(R.id.img_avatar_user);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserAvatar userAvatar = userAvatarList.get(getAdapterPosition());
                    int useravatarID = userAvatar.getAvatar();
                    EditUserActivity.imgavatar.setImageResource(useravatarID);
                }
            });
        }
    }
}
