package huji.postpc.y2021.liorait.calculateroots;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WorkManager workManager = WorkManager.getInstance(this);
        // application singleton
        CalculateRootsApplication application = (CalculateRootsApplication) getApplication();

        // listen to work information
        LiveData<WorkInfo> workInfoByIdLiveData = workManager.getWorkInfoByIdLiveData(application.getWorkerId());

        workInfoByIdLiveData.observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {

                Log.d("work state: " ,""+ workInfo);
                Data progress = workInfo.getProgress();
                long current = progress.getLong("current", -1);
                long counted = progress.getLong("counted", -1);

                if (current != -1){
                    // update in UI
                    Log.d("progress", "current" + current + "of" + counted);
                    TextView t = findViewById(R.id.textView);
                    String s = "current" + current + "of" + counted;
                    t.setText(s);
                }

                if (workInfo.getState().equals(WorkInfo.State.SUCCEEDED)){
                    Data outputData = workInfo.getOutputData();
                    long val = outputData.getLong("counted", -1);
                    Log.d("Activity", "counted " + val);
                }
            }
        });
    } // end of on create
}

/**
 * todo
 * set view holder
 * set adapter
 * create layout for row (cancel)
 * create layout for main activity
 * create layout for add calculation activity
 * build local db
 * save data into sp
 * the local db has a list that updates the adapter's list in any change
 * adapter sends changes to main activity, there it updates the db and then the adapter
 * create work - calculation roots
 * start work manager
 * create order for the list of calculations
 * cancel a calculation
 * delete finished calculation
 * create progress bar that is updates by work
 */