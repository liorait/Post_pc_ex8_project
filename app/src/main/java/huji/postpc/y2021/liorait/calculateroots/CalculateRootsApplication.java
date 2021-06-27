package huji.postpc.y2021.liorait.calculateroots;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.*;
import android.app.Application;
import android.net.Uri;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import huji.postpc.y2021.liorait.calculateroots.workers.Work;

public class CalculateRootsApplication extends Application {
    private UUID id = null;

    @Override
    public void onCreate() {
        super.onCreate();

        // singleton of work manager
        androidx.work.WorkManager workManager = androidx.work.WorkManager.getInstance(this);
        // If not canceling the work continues to re-run
       // workManager.cancelAllWork();
        Constraints.Builder constraintsBuilder = new Constraints.Builder();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(Work.class)
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
    } // end of on create

    UUID getWorkerId(){
        return id;
    }


}
