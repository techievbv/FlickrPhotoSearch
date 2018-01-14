package Api;

import com.example.vaibh.flickrphotosearch.BuildConfig;

import Models.PhotosObject;
import Models.PojoPhotosResponse;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;

/**
 * Created by vaibh on 03-Jan-18.
 */

public interface ApiFlickrPhotoSearch {
    @Headers({
            "Accept: application/json, text/plain, */*",
            "Content-Type: application/json;charset=UTF-8",
            "X-App-Client: android-" + BuildConfig.VERSION_NAME,
            "X-App-Client-Build:" + BuildConfig.VERSION_CODE
    })

    @GET("/services/rest/")
    Call<PojoPhotosResponse> fetchPhotosList(@Query("method") String method, @Query("api_key") String apiKey,
                                             @Query("text") String searchText,
                                             @Query("per_page") int perPage, @Query("page") int page,
                                             @Query("format") String format,
                                             @Query("nojsoncallback") String jsonCallback);
}
