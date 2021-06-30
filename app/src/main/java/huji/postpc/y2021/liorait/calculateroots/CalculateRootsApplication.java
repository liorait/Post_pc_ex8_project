package huji.postpc.y2021.liorait.calculateroots;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.*;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import huji.postpc.y2021.liorait.calculateroots.workers.Work;

public class CalculateRootsApplication extends Application {
    private UUID id = null;
    private LocalDataBase dataBase;
    private SharedPreferences sp;
    private static CalculateRootsApplication instance = null;

    public LocalDataBase getDataBase(){
        return dataBase;
    }
    public static CalculateRootsApplication getInstance(){
        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();

        sp = this.getSharedPreferences("local_work_sp", Context.MODE_PRIVATE);
        instance = this;
        dataBase = new LocalDataBase(this); // pass the current context to allow broadcasts

        // singleton of work manager
        androidx.work.WorkManager workManager = androidx.work.WorkManager.getInstance(this);
    }

    UUID getWorkerId(){
        return id;
    }

    public SharedPreferences getWorkSp() {
        return sp;
    }
}
