package com.example.meowmeow.youtubekids.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meowmeow.youtubekids.Interface.SearchVideo;

import com.example.meowmeow.youtubekids.Model.PlayVideoYTB;
import com.example.meowmeow.youtubekids.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class SearchVideoAdapter extends RecyclerView.Adapter<SearchVideoAdapter.ViewHolder> implements Filterable {

    private Context context;
    private int layout;
    private LayoutInflater mLayoutInflater;
    private ArrayList<SearchVideo> searchVideoList;
    private ArrayList<SearchVideo> searchVideoFullList;

    public SearchVideoAdapter(Context context, int layout, ArrayList<SearchVideo> arrayList) {

        this.context = context;
        this.layout = layout;
        this.searchVideoList = arrayList;
        searchVideoFullList = new ArrayList<>(searchVideoList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgThumbnails;
        public TextView txtTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnails = (ImageView) itemView.findViewById(R.id.img_musicvideo);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_musicvideo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SearchVideo searchVideo = searchVideoList.get(getAdapterPosition());
                    String videoId = searchVideo.getIdVideo();
                    Intent intent = new Intent(context, PlayVideoYTB.class);
                    intent.putExtra("videoId", videoId);
                    context.startActivity(intent);
                    Toast.makeText(context, txtTitle.getText(), Toast.LENGTH_SHORT).show();
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View item = mLayoutInflater.inflate(R.layout.item_custom_video, parent, false);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_video, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SearchVideoAdapter.ViewHolder holder, int position) {
        SearchVideo searchVideo = searchVideoList.get(position);
        holder.txtTitle.setText(searchVideo.getTitle());
        Picasso.with(context).load(searchVideo.getThumbnails()).into(holder.imgThumbnails);
    }

    @Override
    public int getItemCount() {
////        return searchFilteredList.size();
        return searchVideoList.size();
    }

    @Override
    public Filter getFilter() {
        return searchFilterVideo;
    }

    private Filter searchFilterVideo = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<SearchVideo> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(searchVideoFullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (SearchVideo item : searchVideoFullList) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) ;
                    {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            searchVideoList.clear();
            searchVideoList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
}