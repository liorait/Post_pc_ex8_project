package huji.postpc.y2021.liorait.calculateroots;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.UUID;

public class CalculationAdapterClass extends RecyclerView.Adapter<CalculationItemsHolder> {
    private final ArrayList<CalculationItem> list;
    private Context mContext;
    WorkManager workManager = WorkManager.getInstance(mContext);

    public CalculationAdapterClass(Context context){
        this.list = new ArrayList<>();
        this.mContext = context;
    }

    public void addCalculationListToAdapter(ArrayList<CalculationItem> newList){
        this.list.clear();
        this.list.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CalculationItemsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_calculation_item, parent,
                false);
        return new CalculationItemsHolder(view);
    }

    public DeleteClickListener deleteListener;
    public CancelClickListener cancelListener;

    // Create an interface
    public interface DeleteClickListener{
        void onDeleteClick(CalculationItem item);
    }

    // Create an interface
    public interface CancelClickListener{
        void onCancelClick(CalculationItem item);
    }

    public void setDeleteListener(DeleteClickListener listener){
        this.deleteListener = listener;
    }

    public void setCancelListener(CancelClickListener listener){
        this.cancelListener = listener;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CalculationItemsHolder holder, int position) {
        CalculationItem item = this.list.get(position);
        long number = item.getNumber();
        String numberStr = Long.toString(number);
        holder.numberText.setText(numberStr);
        holder.cancelButton.setEnabled(true);
        holder.deleteButton.setEnabled(false);
        holder.deleteButton.setVisibility(View.GONE);
        holder.cancelButton.setVisibility(View.VISIBLE);
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.isDoneCB.setChecked(false);

        holder.progressBar.setProgress(item.getProgress());

        if (item.getIsPrime()){
            holder.rootsTextView.setText("number is prime");
        }

      //  setProgress(item.getProgress(), holder);
      //  holder.setProgress(item.getProgress());
        Log.i("progress in adapter" ,"" + item.getProgress()+"num"+item.getNumber());
        holder.rootsTextView.setText(item.getRootsAsString());
        UUID id = UUID.fromString(item.getId());

        if(item.getStatus().equals("done")){
          //  holder.setProgress(100);
            holder.progressBar.setProgress(100);
            holder.isDoneCB.setChecked(true);
            holder.cancelButton.setVisibility(View.GONE);
            holder.cancelButton.setEnabled(false);

            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setEnabled(true);

        }
        else if (item.getStatus().equals("canceled")){
            holder.rootsTextView.setText("Calculation canceled");
            holder.cancelButton.setVisibility(View.GONE); // todo delete from here?
            holder.cancelButton.setEnabled(false);
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setEnabled(true);
        }
        else{
         //   holder.setProgress(item.getProgress());
            holder.progressBar.setProgress(item.getProgress());
        }


        LiveData<WorkInfo> workInfoByIdLiveData = workManager.getWorkInfoByIdLiveData(id);
        workInfoByIdLiveData.observeForever(new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo.getState().equals(WorkInfo.State.SUCCEEDED)) {
                    Data outputData = workInfo.getOutputData();
                    int progress = outputData.getInt("progress", 0);
                    String is_prime = outputData.getString("is_prime");

                    if (is_prime.equals("true")){
                        item.setRoots(null);
                        item.setIsPrime(true);
                    }
                    item.setProgress(progress);
                    holder.progressBar.setProgress(100);
                  //  holder.progressBar.setProgress(progress);
                    holder.rootsTextView.setText(item.getRootsAsString());
                    holder.rootsTextView.setText(item.getRootsAsString());
                }
                else if (workInfo.getState().equals(WorkInfo.State.RUNNING)) {
                  //  Data outputData = workInfo.getOutputData();
                    int progress = workInfo.getProgress().getInt("progress", 0);
                   // Integer progress = outputData.getInt("progress", 0);
                    item.setProgress(progress); //todo keep?
                    Log.i("progress_in_adapter_obs" ,"" + progress+" num"+item.getNumber());
                    holder.progressBar.setProgress(progress);
                    holder.rootsTextView.setText(item.getRootsAsString());
                }
                else if (workInfo.getState().equals(WorkInfo.State.CANCELLED)){
                    holder.rootsTextView.setText("Calculation canceled");
                    holder.cancelButton.setVisibility(View.GONE); // todo delete from here?
                    holder.cancelButton.setEnabled(false);
                    holder.deleteButton.setVisibility(View.VISIBLE);
                    holder.deleteButton.setEnabled(true);
                }
              //  holder.rootsTextView.setText(item.getRootsAsString());
               // holder.setProgress(item.getProgress());
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            this.list.remove(position);
            deleteListener.onDeleteClick(item);
        });

        holder.cancelButton.setOnClickListener(v -> {
           // holder.rootsTextView.setText("Calculation canceled");
            // todo update in db
          //  item.setStatus("canceled");
            cancelListener.onCancelClick(item);
            holder.cancelButton.setVisibility(View.GONE); // todo delete from here?
            holder.cancelButton.setEnabled(false);
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setEnabled(true);
        });
    }

    @Override
    // return number of calculation items holders
    public int getItemCount() {
        return this.list.size();
    }
}
