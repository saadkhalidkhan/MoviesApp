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

import com.example.hafiz_saad.movies.R;
import com.example.hafiz_saad.movies.model.NowPlayingModel.Result;
import com.example.hafiz_saad.movies.model.NowPlayingModel.RootObject;
import com.example.hafiz_saad.movies.view.activities.MainActivity;
import com.example.hafiz_saad.movies.view.activities.NowPlayingDetailsActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class ShowImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Result> rootObject;
    private int size = 0;
    private String imageBase_URL = "http://image.tmdb.org/t/p/w185//";
    private Context context;
    private ImageLoader imageLoader;
    public ShowImagesAdapter(Context context, List<Result> data, int size) {
        this.context = context;
        this.rootObject = data;
        this.size = size;
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
        imageLoader = ImageLoader.getInstance();
//        setHasStableIds(true);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.images,parent,false);
        return new Items(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,final int position) {
//        Picasso.with(context).load(MainActivity.imageBase_URL+this.rootObject.getResults()
//                .get(position)
//                .getBackdropPath())
//                .into(((Items)holder).poster);
        Uri uri = Uri.parse(imageBase_URL+this.rootObject
                .get(position)
                .getPosterPath());
        ((Items) holder).draweeView.setImageURI(uri);
//        imageLoader.displayImage(MainActivity.imageBase_URL+this.rootObject
//                .get(position)
//                .getPosterPath(),((Items)holder).poster);
//        ((Items) holder).poster.setImageDrawable(null);

//        ((Items)holder).area.setText(deails.get(position).getArea());
    }

    @Override
    public int getItemCount() {
        return this.size;
    }

    public class Items extends RecyclerView.ViewHolder{

        ImageView poster;
        SimpleDraweeView draweeView;
        public Items(View view) {
            super(view);
            draweeView = (SimpleDraweeView) view.findViewById(R.id.image);
//            poster = (ImageView) view.findViewById(R.id.image);
        }
    }
}
