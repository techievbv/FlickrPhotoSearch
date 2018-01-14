package Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vaibh.flickrphotosearch.R;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.internal.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapter.FlickrPhotosAdapter;
import Api.ApiFlickrPhotoSearch;
import Constants.StringConstants;
import MainApp.MainApplication;
import Models.PojoPhotSearch;
import Models.PojoPhotosResponse;
import Utils.Utils;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends Activity {

    int page = 1;
    String searchQuery = "";
    RecyclerView recyclerView;
    AutoCompleteTextView autoCompleteTextView;
    LinearLayoutManager linearLayoutManager;
    TextView fav_btn, empty_result_text;
    List<PojoPhotSearch> photosList;
    FlickrPhotosAdapter flickrPhotosAdapter;
    Call<PojoPhotosResponse> repos;
    private int totalPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViews();
        setListeners();
        initializeDataVariables();
        configureRecyclerView();
        recyclerViewVerticalEndListener();
    }

    private void getViews() {
        recyclerView = findViewById(R.id.photosListview);
        autoCompleteTextView = findViewById(R.id.search);
        fav_btn = findViewById(R.id.fav_btn);
        empty_result_text = findViewById(R.id.empty_result_text);
    }

    private void setListeners() {
        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                empty_result_text.setVisibility(View.GONE);
                searchQuery = textView.getText().toString();
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    autoCompleteTextView.clearFocus();
                    hideKeyboard();
                    photosList.clear();
                    if (Utils.isNetworkAvailable(MainActivity.this)) {
                        callSearchService(searchQuery, page);
                    } else {
                        Utils.showMessageDialog(MainActivity.this);
                    }
                    handled = true;
                }
                return handled;
            }
        });

        fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeDataVariables() {
        linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        photosList = new ArrayList<>();
    }

    private void configureRecyclerView() {
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
    }

    private void recyclerViewVerticalEndListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1)) {
                    onScrolledToBottom();
                }
            }
        });
    }

    private void onScrolledToBottom() {
        if (photosList.size() < totalPhotos) {
            if (Utils.isNetworkAvailable(MainActivity.this)) {
                callSearchService(searchQuery, ++page);
            } else {
                Utils.showMessageDialog(MainActivity.this);
            }
        }
    }

    private void callSearchService(String query, int pageNo) {
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage(StringConstants.LOADING_MSG);
        dialog.setCancelable(false);
        dialog.show();

        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StringConstants.HOSTNAME)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        ApiFlickrPhotoSearch service = retrofit.create(ApiFlickrPhotoSearch.class);
        repos = service.fetchPhotosList(StringConstants.API_METHOD, StringConstants.API_KEY, query, 10, pageNo,
                StringConstants.FORMAT, StringConstants.JSON_CALLBACK);
        repos.enqueue(new Callback<PojoPhotosResponse>() {
            @Override
            public void onResponse(Response<PojoPhotosResponse> response, Retrofit retrofit) {
                try {
                    dialog.dismiss();
                    if (response.body().getPhotosObject().getPhotoList().size() == 0) {
                        empty_result_text.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                    }
                    if (page > 1) {
                        photosList.addAll(response.body().getPhotosObject().getPhotoList());
                        flickrPhotosAdapter.notifyDataSetChanged();
                    } else {
                        if (response.body() != null) {
                            if (Integer.parseInt(response.body().getPhotosObject().getTotal()) > 0) {
                                totalPhotos = Integer.parseInt(response.body().getPhotosObject().getTotal());
                                photosList = response.body().getPhotosObject().getPhotoList();
                                flickrPhotosAdapter = new FlickrPhotosAdapter(photosList, MainActivity.this);
                                recyclerView.setAdapter(flickrPhotosAdapter);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, StringConstants.ERROR_MSG, Toast.LENGTH_LONG);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    empty_result_text.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                empty_result_text.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "No results found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}
