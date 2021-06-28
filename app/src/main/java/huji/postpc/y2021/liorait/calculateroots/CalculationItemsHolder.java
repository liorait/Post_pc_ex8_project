package huji.postpc.y2021.liorait.calculateroots;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalculationItemsHolder extends RecyclerView.ViewHolder {

    protected ArrayList<CalculationItem> calculationsList; // list of calculation items
    TextView numberText;
    TextView rootsTextView;
    TextView cancelButton;
    TextView deleteButton;
    ProgressBar progressBar;

    public CalculationItemsHolder(View view) {
        super(view);
        this.calculationsList = new ArrayList<>();

        // find view by id text view
        numberText = view.findViewById(R.id.numberTextView);
        rootsTextView = view.findViewById(R.id.rootsTextView);
        cancelButton = view.findViewById(R.id.cancelButton);
        deleteButton = view.findViewById(R.id.deleteButton);
        progressBar = view.findViewById(R.id.progressBar);
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
                setProgress(progress + 10);
            }
        });
        thread.start();
    }
}
