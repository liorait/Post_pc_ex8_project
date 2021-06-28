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

    // todo create a list of workers and add tag for each

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();
       // sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp = this.getSharedPreferences("local_work_sp", Context.MODE_PRIVATE);
        instance = this;
        dataBase = new LocalDataBase(this); // pass the current context to allow broadcasts

        // singleton of work manager
        androidx.work.WorkManager workManager = androidx.work.WorkManager.getInstance(this);

        // If not canceling the work continues to re-run
       // workManager.cancelAllWork();
        /**
        Constraints.Builder constraintsBuilder = new Constraints.Builder();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(Work.class)
                .addTag("interesting")
                .setInputData(new Data.Builder().putLong("start", 300).build()).addTag("request")
                .setConstraints(constraintsBuilder.build()).setInitialDelay(20, TimeUnit.MILLISECONDS).build();

        id = request.getId();

        // add a work to list
     //    workManager.enqueue(request);

        // If exists, replace
         workManager.enqueueUniqueWork("count-worker", ExistingWorkPolicy.REPLACE, request);

        // listen to work manager status
        LiveData<WorkInfo> workInfoByIdLiveData = workManager.getWorkInfoByIdLiveData(id);
        workInfoByIdLiveData.observeForever(workInfo -> {
            System.out.println("observing work status " + workInfo);
        });
         */
    } // end of on create

    UUID getWorkerId(){
        return id;
    }

    public SharedPreferences getWorkSp() {
        return sp;
    }



}
