package huji.postpc.y2021.liorait.calculateroots;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Pair;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Comparator;
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
        sortItems(); // todo
        this.sp = context.getSharedPreferences("local_db_calculation_items", Context.MODE_PRIVATE);
        initializeSp();
    }

    // initialize the shared preferences
    private void initializeSp(){
        Set<String> keys = sp.getAll().keySet();
        for (String key: keys){
            String stringRepr = sp.getString(key, null);
            CalculationItem item = CalculationItem.stringToCalculationItem(stringRepr);
            if (item != null){
                items.add(item);
            }
        }
        // update the live data about the changes
        mutableLiveData.setValue(new ArrayList<>(items));
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<CalculationItem> getCopies(){
        sortItems();
        return new ArrayList<>(items);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortItems() {
        this.items.sort(new Comparator<CalculationItem>() {
            @Override
            public int compare(CalculationItem o1, CalculationItem o2) {
                if (o1.getStatus().equals("new") && o2.getStatus().equals("new")) {
                    String num1 = Long.toString(o1.getNumber());
                    String num2 = Long.toString(o2.getNumber());
                    return num2.compareTo(num1);
                }
                return 0;
            }
        });
    }

/**
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void markAsInProgress(String itemId){
        TodoItem newItem = null;
        for (TodoItem item : items){
            if (item.getId().equals(itemId)){
                newItem = new TodoItem(item.getId(), item.getDescription(), item.getStatus());
                newItem.setStatus("IN_PROGRESS");
                newItem.setCreatedDate(item.getCreatedDateAsDate());
                items.add(0, newItem);
                items.remove(item);
                break;
            }
        }
        sortItems();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(newItem.getId(), newItem.itemStringRepresentation());
        editor.apply();
        mutableLiveData.setValue(new ArrayList<>(items));
        sendBroadcastDbChanged();
    }*/

    public void editProgress(UUID id, int progress){
     //   CalculationItem oldItem = this.items.get(id);

       // this.items.remove(id);
    }

    public void updateState(CalculationItem item, String state){
        String id = item.getId();
        CalculationItem old = getById(id);

        CalculationItem newItem = new CalculationItem(old.getId(), old.getNumber(), old.getStatus());
        newItem.setStatus(state);
        newItem.setProgress(old.getProgress());
        newItem.setIsPrime(old.getIsPrime());
        this.items.remove(old);
        this.items.add(this.items.size(), newItem);

        // update sp
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(item.getId(), item.itemStringRepresentation());
        editor.apply();
    }

    public void updatePrime(CalculationItem item, boolean value){
        String id = item.getId();
        CalculationItem old = getById(id);

        CalculationItem newItem = new CalculationItem(old.getId(), old.getNumber(), old.getStatus());
        newItem.setIsPrime(value);
        newItem.setProgress(old.getProgress());
     //   newItem.setStatus(old.getStatus());
        this.items.remove(old);
        this.items.add(this.items.size(), newItem);

        // update sp
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(item.getId(), item.itemStringRepresentation());
        editor.apply();
    }

    public void deleteSp(){
        SharedPreferences.Editor editor = sp.edit();
        editor.clear(); // remove the key
        editor.apply();
        mutableLiveData.setValue(new ArrayList<>(items));
    }

    public void updateRoots(CalculationItem item, Pair<Long, Long> roots){
        String id = item.getId();
        CalculationItem old = getById(id);
        CalculationItem newItem = new CalculationItem(old.getId(), old.getNumber(), old.getStatus());
        newItem.setRoots(roots);
        newItem.setProgress(old.getProgress());
        newItem.setIsPrime(old.getIsPrime());
        this.items.remove(old);
        this.items.add(newItem);

        // update sp
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(item.getId(), item.itemStringRepresentation());
        editor.apply();
    }

    public @Nullable CalculationItem getById(String id){
        if (id == null) return null;
        for (CalculationItem item : items){
            if (item.getId().equals(id)){
                return item;
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addItem(CalculationItem item){
       // String newId = UUID.randomUUID().toString();
       // CalculationItem newItem = new CalculationItem(newId, number, state);

        // all new items are in the beginning
        items.add(0, item);
        sortItems();

        // update sp of the changes
      //  SharedPreferences.Editor editor = sp.edit();
       // editor.putString(newItem.getId(), newItem.itemStringRepresentation());
      //  editor.apply();
        // update sp of the changes
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(item.getId(), item.itemStringRepresentation());
        editor.apply();

        // update the live data of the changed
        mutableLiveData.setValue(new ArrayList<>(items));
      //  sendBroadcastDbChanged(); // send broadcast
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteItem(String itemId){
        CalculationItem toDelete = null;
        for (CalculationItem item : items){
            if (item.getId().equals(itemId)){
                toDelete = item;
                break;
            }
        }
        if (toDelete != null){
            items.remove(toDelete);
        }

        SharedPreferences.Editor editor = sp.edit();
        if (toDelete != null) {
            editor.remove(toDelete.getId()); // remove the key
        }
        editor.apply();

        mutableLiveData.setValue(new ArrayList<>(items));
//        sendBroadcastDbChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sendBroadcastDbChanged(){
        Intent broadcast = new Intent("changed_db");
        broadcast.putExtra("new_list", getCopies());
        context.sendBroadcast(broadcast);
    }
}
