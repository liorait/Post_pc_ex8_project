package huji.postpc.y2021.liorait.calculateroots;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public void onBindViewHolder(@NonNull CalculationItemsHolder holder, int position) {
        CalculationItem item = this.list.get(position);
        holder.numberText.setText(item.getNumber());
        holder.cancelButton.setEnabled(true);
        holder.deleteButton.setEnabled(false);
        holder.deleteButton.setVisibility(View.GONE);
        holder.cancelButton.setVisibility(View.VISIBLE);
        holder.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    // return number of calculation items holders
    public int getItemCount() {
        return this.list.size();
    }
}
