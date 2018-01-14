package Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibh on 04-Jan-18.
 */

public class PhotosObject {

    @SerializedName("page")
    @Expose
    private int page;

    @SerializedName("total")
    @Expose
    private String total;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<PojoPhotSearch> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<PojoPhotSearch> photoList) {
        this.photoList = photoList;
    }

    @SerializedName("photo")
    @Expose
    private List<PojoPhotSearch> photoList = new ArrayList<PojoPhotSearch>();
}
