package huji.postpc.y2021.liorait.calculateroots;

import android.util.Pair;

import java.util.Date;

public class CalculationItem {
    private String number;
    private String status;
    private String itemId;
    private boolean isPrime;
    private Pair<Long, Long> roots;

    public CalculationItem(String id, String number, String status) {
        this.number = number;
        this.status = status;
        this.itemId = id;
        this.isPrime = false;
        this.roots = null;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNumber(String desc) {
        this.number = number;
    }

    public String getId() {
        return itemId;
    }

    public String getNumber() {
        return number;
    }

    public String getStatus() {
        return status;
    }

    public void setId(String id) {
        this.itemId = id;
    }

}
