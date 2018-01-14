package Adapter;

import android.app.Activity;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.vaibh.flickrphotosearch.R;
import com.google.gson.Gson;

import java.util.ArrayList;

import MainApp.MainApplication;
import Utils.Utils;

/**
 * Created by vaibh on 06-Jan-18.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class FavCardViewHolder extends RecyclerView.ViewHolder {
        CardView photoCardView;
        ImageView photo;

        public FavCardViewHolder(View itemView) {
            super(itemView);
            photoCardView = itemView.findViewById(R.id.favPhotoCardView);
            photo = itemView.findViewById(R.id.fav_photo);
        }
    }

    Activity activity;
    ArrayList favPhotosList;

    public FavoritesAdapter(ArrayList list, Activity context) {
        this.favPhotosList = list;
        this.activity = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_favorites, parent, false);
        viewHolder = new FavoritesAdapter.FavCardViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        FavCardViewHolder cardViewHolder = (FavCardViewHolder) viewHolder;
        Glide.with(activity).load(favPhotosList.get(i))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(cardViewHolder.photo);
    }

    @Override
    public int getItemCount() {
        if (favPhotosList == null) {
            return 0;
        } else {
            return favPhotosList.size();
        }
    }
}
