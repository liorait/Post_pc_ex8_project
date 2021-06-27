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

    @NonNull
    @Override
    public CalculationItemsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_calculation_item, parent,
                false);
        return new CalculationItemsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalculationItemsHolder holder, int position) {}

    @Override
    // return number of calculation items holders
    public int getItemCount() {
        return this.list.size();
    }
}
