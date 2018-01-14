package MainApp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vaibh on 06-Jan-18.
 */

public class MainApplication extends Application {
    public static SharedPreferences sp;
    public static SharedPreferences.Editor spEditor;

    @Override
    public void onCreate() {
        super.onCreate();
        sp = this.getSharedPreferences("flickr", Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
