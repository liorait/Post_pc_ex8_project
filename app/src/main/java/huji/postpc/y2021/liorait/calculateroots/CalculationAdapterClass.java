package huji.postpc.y2021.liorait.calculateroots;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalculationAdapterClass extends RecyclerView.Adapter<CalculationItemsHolder> {
    private final ArrayList<CalculationItem> list;
    private Context mContext;

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

       // holder.progressBar.setProgress(item.getProgress());
      //  setProgress(item.getProgress(), holder);
        holder.setProgress(item.getProgress());

       // if (item.getStatus().equals("done")) {
        Pair<Long, Long> roots = item.getRoots();
        if (roots != null) {
            String firstRoot = roots.first.toString();
            String secondRoot = roots.second.toString();
            holder.rootsTextView.setText(firstRoot + " * " + secondRoot);
        }
        else{
            holder.rootsTextView.setText("number is prime");
        }
       // }
    }

    @Override
    // return number of calculation items holders
    public int getItemCount() {
        return this.list.size();
    }
}
