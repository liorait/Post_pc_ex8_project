package huji.postpc.y2021.liorait.calculateroots;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import huji.postpc.y2021.liorait.calculateroots.workers.Work;

public class MainActivity extends AppCompatActivity {

    public CalculationItemsHolder holder = null;
    public CalculationAdapterClass adapter = null;
    public LocalDataBase dataBase = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerCalculationItemsList); // finds the recycler view
        adapter = new CalculationAdapterClass(this);

        if (dataBase == null){
            dataBase = CalculateRootsApplication.getInstance().getDataBase();
            dataBase.getCopies();
        }

        WorkManager workManager = WorkManager.getInstance(this);
        // application singleton
        CalculateRootsApplication application = (CalculateRootsApplication) getApplication();
        /**
        dataBase.publicLiveData.observe(this, new Observer<List<CalculationItem>>() {
            @Override
            public void onChanged(List<TodoItem> todoItems) {
                if(todoItems.size() == 0){
                    adapter.addTodoListToAdapter(new ArrayList<>());
                }
                else{
                    adapter.addTodoListToAdapter(new ArrayList<>(todoItems));
                    //adapter.notifyDataSetChanged();
                }
            }
        });*/

        if (holder == null) {
            holder = new CalculationItemsHolder(recyclerView);
        }
        ArrayList<CalculationItem> items = dataBase.getCopies();

        // Create the adapter
        adapter.addCalculationListToAdapter(items);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false));

        EditText textInsertTask = findViewById(R.id.editTextInsertTask);
        FloatingActionButton buttonAddToDoItem = findViewById(R.id.buttonCreateNewCalculation);

        buttonAddToDoItem.setOnClickListener(v -> {
            String userInputString = textInsertTask.getText().toString();

            // parse string to long
            long userInputLong = 0;
            try {
                userInputLong = Long.parseLong(userInputString);
            }
            catch (NumberFormatException e){
                //Toast.makeText(this, "could not convert String to Long", Toast.LENGTH_SHORT).show();
                Log.e("error", "could not convert String to Long");
                return;
            }

            // create new worker
            OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(Work.class)
                    .addTag("new_work")
                    .setInputData(new Data.Builder().putLong("number", userInputLong).build()).build();

           // workManager.enqueueUniqueWork()
            workManager.enqueue(request);

            // add to the adapter
            // If the text isn't empty, creates a new calculation object
            if (!userInputString.equals("")){
                dataBase.addItem(userInputString, "IN_PROGRESS");
                ArrayList<CalculationItem> list = dataBase.getCopies();
                adapter.addCalculationListToAdapter(list);
                adapter.notifyDataSetChanged();
                textInsertTask.setText("");
            }
        });

        // listen to work information
        /**
        LiveData<WorkInfo> workInfoByIdLiveData = workManager.getWorkInfoByIdLiveData(application.getWorkerId());

        workInfoByIdLiveData.observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {

                Log.d("work state: " ,""+ workInfo);
                Data progress = workInfo.getProgress();
                long current = progress.getLong("current", -1);
                long total = progress.getLong("total", -1);

                if (current != -1){
                    // update in UI
                    Log.wtf("progress", "current" + current + "of" + total);
                    System.out.println("current" + current + "of" + total);
                    TextView t = findViewById(R.id.textView);
                    String s = "current " + current + " of " + total;
                    t.setText(s);
                }

                if (workInfo.getState().equals(WorkInfo.State.SUCCEEDED)){
                    Data outputData = workInfo.getOutputData();
                    long val = outputData.getLong("counted", -1);
                    Log.d("Activity", "counted " + val);
                }
            }
        });
         */
        LiveData<List<WorkInfo>> workers_interesting = workManager.getWorkInfosByTagLiveData("new_work");
        workers_interesting.observe(this, new Observer<List<WorkInfo>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<WorkInfo> workInfos) {
                for (WorkInfo w : workInfos){
                   // if (w.getState().equals(WorkInfo.State.RUNNING)){}
                }
            }
        });

    } // end of on create
}

/**
 * todo
 * set view holder - done
 * set adapter - done
 * create adapter class
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