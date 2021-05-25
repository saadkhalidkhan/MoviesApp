package com.example.hafiz_saad.movies.view.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hafiz_saad.movies.R;
import com.example.hafiz_saad.movies.model.NowPlayingModel.Result;
import com.example.hafiz_saad.movies.model.NowPlayingModel.RootObject;
import com.example.hafiz_saad.movies.view.activities.MainActivity;
import com.example.hafiz_saad.movies.view.activities.NowPlayingDetailsActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    RootObject rootObject;
    private List<Result> results;
    private int size = 0;
    private Context context;
    private String imageBase_URL = "http://image.tmdb.org/t/p/w185//";
    private ImageLoader imageLoader;

    public RecycleAdapter(Context context, List<Result> results, RootObject data, int size) {
        this.context = context;
        this.rootObject = data;
        this.results = results;
        this.size = size;
        Fresco.initialize(context);
//        setHasStableIds(true);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.playing_movies, parent, false);
        return new Items(view);
    }

    //
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ((Items) holder).title.setText(results.get(position).getTitle());
        ((Items) holder).overview.setText(results.get(position).getOverview());
        ((Items) holder).releaseDate.setText(results.get(position).getReleaseDate());
        Uri uri = Uri.parse(imageBase_URL + results
                .get(position)
                .getPosterPath());
        ((Items) holder).draweeView.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return this.results.size();
    }

    public class Items extends RecyclerView.ViewHolder {
        TextView title;
        TextView releaseDate;
        TextView overview;
        TextView moreInfo;
        ImageView poster;
        SimpleDraweeView draweeView;

        public Items(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            releaseDate = (TextView) view.findViewById(R.id.release_date);
            overview = (TextView) view.findViewById(R.id.overview);
            moreInfo = (TextView) view.findViewById(R.id.more);
//            poster = (ImageView) view.findViewById(R.id.image);
            draweeView = (SimpleDraweeView) view.findViewById(R.id.poster);
        }
    }
}
