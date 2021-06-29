package huji.postpc.y2021.liorait.calculateroots;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class CalculationItemsHolder extends RecyclerView.ViewHolder {

    protected ArrayList<CalculationItem> calculationsList; // list of calculation items
    TextView numberText;
    TextView rootsTextView;
    TextView cancelButton;
    TextView deleteButton;
    ProgressBar progressBar;
    CheckBox isDoneCB;
    LocalDataBase dataBase;

    public CalculationItemsHolder(View view) {
        super(view);
        this.calculationsList = new ArrayList<>();
        dataBase = CalculateRootsApplication.getInstance().getDataBase();

        // find view by id text view
        numberText = view.findViewById(R.id.numberTextView);
        rootsTextView = view.findViewById(R.id.rootsTextView);
        cancelButton = view.findViewById(R.id.cancelButton);
        deleteButton = view.findViewById(R.id.deleteButton);
        progressBar = view.findViewById(R.id.progressBar);
        isDoneCB = view.findViewById(R.id.checkBox);
    }

    public ArrayList<CalculationItem> getCurrentItems() {
        return this.calculationsList;
    }

    public void setProgress(int progress){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }

              //  progressBar.setProgress(progress + 10);
                Log.i("thread_progress",""+ progress);
                if (progress + 10 <= 100) {
                    //setProgress(progress + 10);
                }
            }
        });
        thread.start();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<String> saveItemsRepresentation(){
        // goes over the list items and for each item create string representation and save
        ArrayList<String> itemsStrList = new ArrayList<>();

        ArrayList<CalculationItem> dbList = new ArrayList(dataBase.getCopies());

        for (int i = 0; i < dbList.size(); i++){
            String itemStr = dbList.get(i).itemStringRepresentation();
            itemsStrList.add(itemStr);
        }
        return itemsStrList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Serializable saveState() {

        // create new holderState
        CalculationItemsHolderState holder_state = new CalculationItemsHolderState();

        // save the list into the state
        holder_state.itemsRepresentationList = saveItemsRepresentation();
        return holder_state;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void convertStringListToCalculationList(ArrayList<String> listStr) {
        for (int i = 0; i < listStr.size(); i++)
        {
            // create a new item
            CalculationItem newItem = CalculationItem.stringToCalculationItem(listStr.get(i));
            dataBase.addItem(newItem);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadState(Serializable prevState){
        if (!(prevState instanceof CalculationItemsHolderState)) {
            return; // ignore
        }
        CalculationItemsHolderState casted = (CalculationItemsHolderState) prevState;
        // convert the represented items list to an ItemTodo list
        convertStringListToCalculationList(casted.itemsRepresentationList);
    }

    private static class CalculationItemsHolderState implements Serializable {
        private ArrayList<String> itemsRepresentationList;
        private String newTaskTest;
    }
}
