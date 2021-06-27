package huji.postpc.y2021.liorait.calculateroots.workers;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Date;

public class Work extends Worker {
    // declares work

    public Work(@NonNull Context context, @NonNull WorkerParameters parameters){
        super(context, parameters);
    }


    @NonNull
    @Override
    public Result doWork() {




        /**
        long start = getInputData().getLong("start",200);
        long total = start;
        // this code runs asyncronically on a background thread
        // todo here calculate roots

        for (int i = 0; i < total; i++){
         //   for (int j = 0; j < 100; j++) {
                Log.wtf("count work tag", "" + i);

                // Send status updates
                this.setProgressAsync(
                        new Data.Builder().putLong("current", i).putLong("total", total).build()
                );
          // }
        }
*/
        return Result.success(new Data.Builder().putLong("counted", 100000000L).build());
        // todo return Result.success/retry/failure

    }

    private Pair<Long, Long> calculateRoots(long numberToCalculateRootsFor){
           // long timeStartMs = System.currentTimeMillis();
            //long endTime = timeStartMs + 20000L;

        int i = 2;
        //    long currentTimeAfter = System.currentTimeMillis();

        while (i <= numberToCalculateRootsFor / 2){ //&& (currentTimeAfter < endTime)){
            long root = (long) (numberToCalculateRootsFor / i);
            if (numberToCalculateRootsFor % i == 0) {
                Long j = (long) (numberToCalculateRootsFor / root);
                Pair<Long, Long> newPair = new Pair<>(j, root);
               // isFinishedCalculation = true;
                return newPair;
            }i++;
          //  currentTimeAfter = System.currentTimeMillis();
        }
        return null;
    }
}
