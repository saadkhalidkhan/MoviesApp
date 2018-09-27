package com.example.hafiz_saad.movies.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hafiz_saad.movies.R;
import com.example.hafiz_saad.movies.model.VideoModel.VideoResult;

import java.util.List;

public class VideosListAdapter   extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<VideoResult> videoResults;
    private int size = 0;
    private Context context;

    public VideosListAdapter(Context context,List<VideoResult> data, int size) {
        this.context = context;
        this.videoResults = data;
        this.size = size;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.video_list,parent,false);
        return new Items(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((Items)holder).trailer.setText(this.videoResults.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return this.size;
    }
    public class Items extends RecyclerView.ViewHolder{
        TextView trailer;
        public Items(View view) {
            super(view);
            trailer = (TextView) view.findViewById(R.id.trailer);

        }
    }
}
