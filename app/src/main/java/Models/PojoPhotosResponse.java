package Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vaibh on 04-Jan-18.
 */

public class PojoPhotosResponse {
    @SerializedName("photos")
    @Expose
    private PhotosObject photosObject;

    public PhotosObject getPhotosObject() {
        return photosObject;
    }

    public void setPhotosObject(PhotosObject photosObject) {
        this.photosObject = photosObject;
    }
}
