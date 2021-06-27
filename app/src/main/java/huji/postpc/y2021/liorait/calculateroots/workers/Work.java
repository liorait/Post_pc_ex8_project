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
    private Integer progress;
    private Integer total = 100;
    // declares work

    public Work(@NonNull Context context, @NonNull WorkerParameters parameters){
        super(context, parameters);
        progress = 0;
        // Send status updates
        this.setProgressAsync(new Data.Builder().putInt("progress", progress).putInt("total", total).build());

    }

    @NonNull
    @Override
    public Result doWork() {
        Long numberToCalculateRootsFor = getInputData().getLong("number", 1);
        Pair<Long, Long> roots = calculateRoots(numberToCalculateRootsFor);
        if (roots == null){
            String number = Long.toString(numberToCalculateRootsFor);
            String rootsStr = " " + "1" + "," + number;
            return Result.success(new Data.Builder().putString("is_prime", "true")
                    .putLong("first_root", 1)
                    .putLong("second_root", numberToCalculateRootsFor).build());
                  //  .putString("roots", rootsStr).build());

        }
        else{
           // String rootsStr = " " + roots.first.toString() + "," + roots.second.toString();
            return Result.success(new Data.Builder().putString("is_prime", "false")
                    .putLong("first_root", roots.first)
                    .putLong("second_root", roots.second).build());
                 //   .putString("roots", rootsStr).build());
        }


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
       // return Result.success(new Data.Builder().putLong("counted", 100000000L).build());
        // todo return Result.success/retry/failure

    }

    private Pair<Long, Long> calculateRoots(long numberToCalculateRootsFor){
           // long timeStartMs = System.currentTimeMillis();
            //long endTime = timeStartMs + 20000L;

        int i = 2;

        while (i <= numberToCalculateRootsFor / 2){
            Integer currentProgress = (int) Math.ceil(((double) i / (numberToCalculateRootsFor/2.0)) * 100);
            //if (newProgress != progress){
          //  progress = currentProgress;
            this.setProgressAsync(new Data.Builder().putInt("progress",  currentProgress).build());
            //}

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
        // If roots were not found, prime number
      //  Pair<Long, Long> pair = new Pair<>(1, numberToCalculateRootsFor);
       // return pair;
    }
}
