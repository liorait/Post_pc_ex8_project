package huji.postpc.y2021.liorait.calculateroots.workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Date;

import huji.postpc.y2021.liorait.calculateroots.CalculateRootsApplication;

public class Work extends Worker {
    private Integer progress;
    private Integer total = 100;
    private SharedPreferences workSp;
    private boolean isDone;
    private long beginTime;
    private String PROGRESS = "progress";
    private boolean isRetry = false;
    // declares work

    public Work(@NonNull Context context, @NonNull WorkerParameters parameters){
        super(context, parameters);
        progress = 0;
        // Send status updates
        this.setProgressAsync(new Data.Builder().putInt(PROGRESS, progress).putInt("total", total).build());
        CalculateRootsApplication application = (CalculateRootsApplication) getApplicationContext();
        workSp = application.getWorkSp();
        this.isDone = false;
        this.beginTime = System.currentTimeMillis();
    }

    @NonNull
    @Override
    public Result doWork() {

        long numberToCalculateRootsFor = getInputData().getLong("number", -1);

        if (numberToCalculateRootsFor == -1){
            return Result.failure();
        }

        Pair<Long, Long> roots = calculateRoots(numberToCalculateRootsFor);

        // If couldn't find roots, the number is prime or the work has stopped
        if (roots == null){
            if (this.isRetry){
                return Result.retry();
            }
            if (this.isDone) {
                return Result.success(new Data.Builder().putString("is_prime", "true")
                        .putLong("first_root", 1)
                        .putLong("second_root", numberToCalculateRootsFor).build());
            }
            else{
                // work stopped
                Log.i("stopped", "work_stopped");
                return Result.failure();
            }
        }
        else{
            return Result.success(new Data.Builder().putString("is_prime", "false")
                    .putLong("first_root", roots.first)
                    .putLong("second_root", roots.second).build());
        }
    }

    private Pair<Long, Long> calculateRoots(long numberToCalculateRootsFor) {

        long i = workSp.getLong("reached_calculation_number" + numberToCalculateRootsFor, 2);
        Log.i("progress roots from",""+ i +  " num" + numberToCalculateRootsFor);
        long total = (long) Math.sqrt(numberToCalculateRootsFor);

        while (i <= total){

            Log.i("current i in work", "i " + i + " num" + numberToCalculateRootsFor);
            long square = (long) Math.ceil(Math.sqrt(numberToCalculateRootsFor));

            if (i % 20 == 0) {

                int currentProgress = (int) Math.ceil(((double) i / square) * 100);
                Log.i("current prog in work", "" + currentProgress + "num" + numberToCalculateRootsFor);
                Log.i("progress in work", "" + currentProgress + "i" + i + " num" + numberToCalculateRootsFor);
                this.setProgressAsync(new Data.Builder().putInt("progress", currentProgress).build());
            }

            if (this.isStopped()){

                // Save current number reached to
                SharedPreferences.Editor editor = workSp.edit();
                editor.putLong("reached_calculation_number" + numberToCalculateRootsFor, i);
                editor.apply();
                this.isDone = false;
                return null;
            }

            long timeout = 540000L;
            if (System.currentTimeMillis() - beginTime > timeout){

                // Save current number reached to
                SharedPreferences.Editor editor = workSp.edit();
                editor.putLong("reached_calculation_number" + numberToCalculateRootsFor, i);
                editor.apply();
                isRetry = true;
                return null;
            }
            if(i % 3000L==0){
                SharedPreferences.Editor editor = workSp.edit();
                editor.putLong("reached_calculation_number" + numberToCalculateRootsFor, i);
                editor.apply();
            }

            long root = (long) (numberToCalculateRootsFor / i);

            if (numberToCalculateRootsFor % i == 0) {
                Long j = (long) (numberToCalculateRootsFor / root);
                Pair<Long, Long> newPair = new Pair<>(j, root);

                // save to sp
                SharedPreferences.Editor editor = workSp.edit();
                editor.putLong("reached_calculation_number" + numberToCalculateRootsFor, i);
                editor.apply();

                this.isDone = true;
                return newPair;
            }

            try {
                Thread.sleep(1000L);
            }
            catch(InterruptedException e){
                Log.e("delay", " error in thread delay");
            }

            SharedPreferences.Editor editor = workSp.edit();
            editor.putLong("reached_calculation_number" + numberToCalculateRootsFor, i);
            editor.apply();
            i++;
        } // end of while

        this.isDone = true;
        return null;
    }
}
