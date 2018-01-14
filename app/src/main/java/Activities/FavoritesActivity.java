package Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.vaibh.flickrphotosearch.R;
import com.google.gson.Gson;
import com.squareup.okhttp.internal.Util;

import java.util.ArrayList;
import java.util.List;

import Adapter.FavoritesAdapter;
import MainApp.MainApplication;
import Utils.Utils;

/**
 * Created by vaibh on 06-Jan-18.
 */

public class FavoritesActivity extends FragmentActivity {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_activity);
        getViews();
        linearLayoutManager = new LinearLayoutManager(FavoritesActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new FavoritesAdapter(new Gson().fromJson(MainApplication.sp.getString("name", ""),
                ArrayList.class), FavoritesActivity.this));
    }

    private void getViews() {
        recyclerView = (RecyclerView) findViewById(R.id.favsListview);
    }
}
