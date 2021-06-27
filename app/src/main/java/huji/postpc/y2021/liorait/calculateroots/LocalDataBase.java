package huji.postpc.y2021.liorait.calculateroots;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class LocalDataBase {
    private final ArrayList<CalculationItem> items = new ArrayList<>();
    private final Context context;
    private final SharedPreferences sp;
    private final MutableLiveData<List<CalculationItem>> mutableLiveData = new MutableLiveData<>();
    public final LiveData<List<CalculationItem>> publicLiveData = mutableLiveData;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public LocalDataBase(Context context){
        this.context = context;
        //sortItems(); // todo
        this.sp = context.getSharedPreferences("local_db_calculation_items", Context.MODE_PRIVATE);
     //   initializeSp();
    }

    // initialize the shared preferences
    /**
    private void initializeSp(){
        Set<String> keys = sp.getAll().keySet();
        for (String key: keys){
            String stringRepr = sp.getString(key, null);
            CalculationItem item = CalculationItem.stringToToDoItem(stringRepr);
            if (item != null){
                items.add(item);
            }
        }
        // update the live data about the changes
        mutableLiveData.setValue(new ArrayList<>(items));
    }
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<CalculationItem> getCopies(){
        //sortItems();
        return new ArrayList<>(items);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addItem(CalculationItem item){
       // String newId = UUID.randomUUID().toString();
       // CalculationItem newItem = new CalculationItem(newId, number, state);
        items.add(0, item);
        //sortItems();

        // update sp of the changes
      //  SharedPreferences.Editor editor = sp.edit();
       // editor.putString(newItem.getId(), newItem.itemStringRepresentation());
      //  editor.apply();

        // update the live data of the changed
        mutableLiveData.setValue(new ArrayList<>(items));
      //  sendBroadcastDbChanged(); // send broadcast
    }

}
