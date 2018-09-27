package com.example.hafiz_saad.movies.controller.services.Util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener{
    private LinearLayoutManager linearLayoutManager;
    private Context context;
    public PaginationScrollListener(Context context, LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
        this.context = context;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemcount = linearLayoutManager.getChildCount();
        int totalItemcount = linearLayoutManager.getItemCount();
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        if(!loading() && !lastPage()){
            if((visibleItemcount + firstVisibleItemPosition) >= totalItemcount && firstVisibleItemPosition >= 0){
                loadMoreItems();
            }
        }
        else if( lastPage()){
//            Toast.makeText(context, "no more data!", Toast.LENGTH_SHORT).show();
        }

    }

    protected abstract void loadMoreItems();
    public abstract float getTotalPageCount();
    public abstract boolean lastPage();
    public abstract boolean loading();
}

