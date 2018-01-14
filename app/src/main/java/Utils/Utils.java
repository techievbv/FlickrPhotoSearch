package Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.TextView;

import com.example.vaibh.flickrphotosearch.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.StringTokenizer;

import Constants.StringConstants;

/**
 * Created by vaibh on 05-Jan-18.
 */

public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if ((activeNetworkInfo != null) && (activeNetworkInfo.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    public static void showMessageDialog(Activity activity) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage(StringConstants.INTERNET_NOT_WORKING);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static String urlBuilder(String farm_id, String id, String secret, String server) {
        String imageUrl = new StringBuilder().append("https://farm").append(farm_id).
                append(".staticflickr.com/").append(server).append("/").append(id).append("_").append(secret).append(".jpg").toString();
        return imageUrl;
    }
}
