package huji.postpc.y2021.liorait.calculateroots;

import android.util.Pair;

import java.util.Date;
import java.util.UUID;

public class CalculationItem {
    private long number;
    private String status;
    private UUID itemId;
    private boolean isPrime;
    private int progress;
    private int total_progress;
    private Pair<Long, Long> roots;

    public CalculationItem(UUID id, long number, String status) {
        this.number = number;
        this.status = status;
        this.itemId = id;
        this.isPrime = false;
        this.roots = null;
        this.total_progress = 100;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public UUID getId() {
        return itemId;
    }

    public long getNumber() {
        return number;
    }

    public String getStatus() {
        return status;
    }

    public void setId(UUID id) {
        this.itemId = id;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Pair<Long, Long> getRoots() {
        return roots;
    }

    public void setRoots(Pair<Long, Long> roots) {
        this.roots = roots;
    }

}
