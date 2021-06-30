package huji.postpc.y2021.liorait.calculateroots;

import androidx.annotation.NonNull;
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
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import huji.postpc.y2021.liorait.calculateroots.workers.Work;

public class MainActivity extends AppCompatActivity {

    public CalculationItemsHolder holder = null;
    public CalculationAdapterClass adapter = null;
    public LocalDataBase dataBase = null;
    String NEW = "new";
    WorkManager workManager = WorkManager.getInstance(this); // gets the work manager singleton

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerCalculationItemsList); // finds the recycler view
        adapter = new CalculationAdapterClass(this);

        if (dataBase == null){
            dataBase = CalculateRootsApplication.getInstance().getDataBase();
        }
       // dataBase.deleteSp();// todo delete
      //  workManager.cancelAllWork();

        // application singleton
        CalculateRootsApplication application = (CalculateRootsApplication) getApplication();

        dataBase.publicLiveData.observe(this, new Observer<List<CalculationItem>>() {
            @Override
            public void onChanged(List<CalculationItem> items) {
                if(items.size() == 0){
                    adapter.addCalculationListToAdapter(new ArrayList<>());
                }
                else{
                    adapter.addCalculationListToAdapter(new ArrayList<>(items));
                }
            }
        });

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

        adapter.setDeleteListener(new CalculationAdapterClass.DeleteClickListener() {
            public void onDeleteClick(CalculationItem item) {
                dataBase.deleteItem(item.getId());
                UUID id = UUID.fromString(item.getId());
                //workManager.cancelWorkById(id); deletes only what is done
                adapter.notifyDataSetChanged();
                //adapter.addCalculationListToAdapter(dataBase.getCopies());
            }
        });

        adapter.setCancelListener(new CalculationAdapterClass.CancelClickListener() {
            public void onCancelClick(CalculationItem item) {
                UUID id = UUID.fromString(item.getId());
                workManager.cancelWorkById(id);
                dataBase.updateState(item, "canceled");
                adapter.notifyDataSetChanged();
                //adapter.addCalculationListToAdapter(dataBase.getCopies());
            }
        });

        // Adds a new calculation item
        buttonAddToDoItem.setOnClickListener(v -> {

            String userInputString = textInsertTask.getText().toString();
            long userInputLong = parseStringToLong(userInputString);
            if (userInputLong == -1)
            {
                Toast.makeText(this, "could not convert String to Long", Toast.LENGTH_SHORT).show();
            }

            // add to the adapter
            // If the text isn't empty, creates a new calculation object
            if (!userInputString.equals("") && (userInputLong != -1)) {

                // create new worker
                OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(Work.class)
                        .addTag("new_work")
                        .setInputData(new Data.Builder().putLong("number", userInputLong).build()).build();

                // workManager.enqueueUniqueWork()
                workManager.enqueue(request);

                UUID requesId = request.getId();

                // live data of the status of the worker todo delete
                LiveData<WorkInfo> workInfoByIdLiveData = workManager.getWorkInfoByIdLiveData(requesId);
                workInfoByIdLiveData.observeForever(new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        WorkInfo.State state = workInfo.getState();
                        System.out.println("state of " + requesId.toString()+ "is " + state );
                    }
                });

                CalculationItem item = new CalculationItem(request.getId().toString(), userInputLong, NEW);
                dataBase.addItem(item); // Adds item to local db

                ArrayList<CalculationItem> list = dataBase.getCopies();
                adapter.addCalculationListToAdapter(list);
                adapter.notifyDataSetChanged();
                textInsertTask.setText("");

                // listen to the current item's work information
                observeCalculationItemWork(item);
            }
        });

    } // end of on create

    private long parseStringToLong(String userInputString){

        // parse string to long
        long userInputLong = 0;
        try {
            userInputLong = Long.parseLong(userInputString);
        }
        catch (NumberFormatException e){
            Toast.makeText(this, "could not convert String to Long", Toast.LENGTH_SHORT).show();
            Log.e("error", "could not convert String to Long");
            return -1;
        }
        return userInputLong;
    }

    // listen to work information
    private void observeCalculationItemWork(CalculationItem item){
        UUID workId = UUID.fromString(item.getId());

        workManager.getWorkInfoByIdLiveData(workId).observe(this, new Observer<WorkInfo>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo.getState().equals(WorkInfo.State.SUCCEEDED)){
                    Data outputData = workInfo.getOutputData();
                    String is_prime = outputData.getString("is_prime");
                    item.setStatus("done");
                    dataBase.updateState(item, "done");
                    adapter.notifyDataSetChanged();
                    item.setProgress(100);

                    // If the number isn't prime, show it's roots in roots text view
                    if (is_prime.equals("false")) {
                        long first_root = outputData.getLong("first_root", -1);
                        long second_root = outputData.getLong("second_root", -1);
                        Pair<Long, Long> roots = new Pair<>(first_root, second_root);
                        item.setRoots(roots);
                        item.setIsPrime(false);
                        dataBase.updateState(item, "done");
                       // item.setStatus("done");
                        dataBase.updatePrime(item, false);
                        dataBase.updateRoots(item, roots);
                        adapter.notifyDataSetChanged();
                    }
                    else if (is_prime.equals("true")){
                        //TextView rootsTextView = findViewById(R.id.rootsTextView);
                        //rootsTextView.setText("number is prime");
                      //  item.setStatus("done");
                        item.setRoots(null);
                        dataBase.updateState(item, "done");
                        dataBase.updatePrime(item, true);
                        item.setIsPrime(true);
                        ArrayList<CalculationItem> list = dataBase.getCopies();
                        adapter.addCalculationListToAdapter(list);
                        adapter.notifyDataSetChanged();
                    }
                }
                // update progress bar
                else if (workInfo.getState().equals(WorkInfo.State.RUNNING)){
                    int progress = workInfo.getProgress().getInt("progress", 0);
                    Log.i("got progress in main", "" + progress + "of num" + item.getNumber());
                  //  setProgressForItem(progress);
                 //   dataBase.editProgress(item.getId(), progress);
                    item.setProgress(progress);
                   // ArrayList<CalculationItem> list = dataBase.getCopies();
                  //  adapter.addCalculationListToAdapter(list);
                    adapter.notifyDataSetChanged();
                }
                else if (workInfo.getState().equals(WorkInfo.State.CANCELLED)) {
                    //holder.rootsTextView.setText("Calculation canceled");
                    item.setStatus("canceled");
                   // dataBase.updateState(item, "done");
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    // flip screen
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("saved_state", holder.saveState());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Serializable saved_output = savedInstanceState.getSerializable("saved_state");
        holder.loadState(saved_output);
        adapter.addCalculationListToAdapter(dataBase.getCopies());
    }
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