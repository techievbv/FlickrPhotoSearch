package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import java.util.List;


import Constants.StringConstants;
import MainApp.MainApplication;
import Models.PojoPhotSearch;
import Utils.Utils;

/**
 * Created by vaibh on 04-Jan-18.
 */

public class FlickrPhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView photoCardView;
        ImageView photo;
        TextView photoTitle;

        public CardViewHolder(View itemView) {
            super(itemView);
            photoCardView = itemView.findViewById(R.id.photoCardView);
            photo = itemView.findViewById(R.id.photo);
            photoTitle = itemView.findViewById(R.id.photoTitle);
        }
    }


    Activity activity;
    List<PojoPhotSearch> photosList;
    private GestureDetectorCompat gestureDetector;
    String imageUrl = "";
    ArrayList savedList = new Gson().fromJson(MainApplication.sp.getString("name", ""),
            ArrayList.class);


    public FlickrPhotosAdapter(List<PojoPhotSearch> photosList, Activity context) {
        this.photosList = photosList;
        this.activity = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_flickr_photo, parent, false);
        viewHolder = new CardViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        imageUrl = Utils.urlBuilder(photosList.get(i).getFarm(), photosList.get(i).getId(),
                photosList.get(i).getSecret(), photosList.get(i).getServer());

        final CardViewHolder cardViewHolder = (CardViewHolder) viewHolder;
        cardViewHolder.photoTitle.setText(photosList.get(i).getTitle());
        Glide.with(activity).load(imageUrl)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(cardViewHolder.photo);

        onCardClick(cardViewHolder.photoCardView, i, imageUrl);
    }

    private void onCardClick(CardView cv, final int i, final String image) {
        gestureDetector = new GestureDetectorCompat(activity, this);
        gestureDetector.setOnDoubleTapListener(this);
        cv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                imageUrl = Utils.urlBuilder(photosList.get(i).getFarm(), photosList.get(i).getId(),
                        photosList.get(i).getSecret(), photosList.get(i).getServer());
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        if (photosList == null) {
            return 0;
        } else {
            return photosList.size();
        }
    }


    /* Gesture events */
    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        if (savedList == null) {
            savedList = new ArrayList();
        }
        if (!savedList.contains(imageUrl)) {
            storeFavoritePhotoOffline(imageUrl);
            Toast.makeText(activity, "Added to Favorites", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Image is already in Favorites", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        if (savedList == null) {
            savedList = new ArrayList();
        }
        if (savedList.contains(imageUrl)) {
            removeFavoritePhoto(imageUrl);
            Toast.makeText(activity, "Removed from Favorites", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Image is not in Favorites", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }


    private void storeFavoritePhotoOffline(String image) {
        savedList.add(image);
        MainApplication.spEditor.putString("name", new Gson().toJson(savedList)).apply();
    }

    private void removeFavoritePhoto(String image) {
        savedList.remove(image);
        MainApplication.spEditor.putString("name", new Gson().toJson(savedList)).apply();
    }
}
