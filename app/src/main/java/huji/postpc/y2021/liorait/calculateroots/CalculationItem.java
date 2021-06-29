package huji.postpc.y2021.liorait.calculateroots;

import android.util.Log;
import android.util.Pair;

import java.io.Serializable;
import java.util.UUID;

public class CalculationItem implements Serializable {
    private long number;
    private String status;
    private String itemId;
    private boolean isPrime;
    private int progress;
    private int total_progress;
    private Pair<Long, Long> roots;

    public CalculationItem(String id, long number, String status) {
        this.number = number;
        this.status = status;
        this.itemId = id;
        this.isPrime = false;
        this.roots = null;
        this.total_progress = 100;
    }

    public void setIsPrime(boolean prime) {
        this.isPrime = prime;
    }

    public boolean getIsPrime() {
        return this.isPrime;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getId() {
        return itemId;
    }

    public long getNumber() {
        return number;
    }

    public String getStatus() {
        return status;
    }

    public void setId(String id) {
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

    protected String itemStringRepresentation() {
        String rootsRepr = "";
        if (roots == null) {
            if (isPrime){
                rootsRepr = "number is prime";
            }
            else{
                rootsRepr = "calculating roots..";
            }
        }
        else {
            rootsRepr = roots.first.toString() + "!" + roots.second.toString();
        }
        String numberStr = Long.toString(this.number);
        String prime = "";
        if (isPrime){prime="true";}else{prime="false";}

        String repr = numberStr + "/" + this.status + "/" + this.itemId + "/" +
        prime + "/" + Integer.toString(progress) + "/" + Integer.toString(total_progress) + "/" + rootsRepr;

        return repr;
    }

    public String getRootsAsString(){
        String rootsStr = "";

        //Pair<Long, Long> roots = this.getRoots();
        if (this.roots != null) {
            String firstRoot = roots.first.toString();
            String secondRoot = roots.second.toString();
            return firstRoot + " * " + secondRoot;
        }
        else if ((roots == null) && (this.isPrime)){
             return "number is prime";
        }
        return "calculating roots..";
    }

    static protected CalculationItem stringToCalculationItem(String stringRepr) {
        if (stringRepr == null) return null;
        try {
            String val = stringRepr;
            String[] split = val.split("/");
            String number = split[0];
            String status = split[1];
            String itemId = split[2];
            String isPrime = split[3];
            String progress = split[4];
            String total = split[5];
            String rootsRepr = split[6];

            // parse string to long
            long userInputLong = 0;
            try {
                userInputLong = Long.parseLong(number);
            }
            catch (NumberFormatException e){
                Log.e("error", "could not convert String to Long");
                return null;
            }

            // parse string to UUID
            //UUID id = UUID.fromString(itemId);
            boolean prime = false;
            if (isPrime.equals("true")) {
                prime = true;
            }

            int progressInt = Integer.parseInt(progress);
            int totalProgressInt = Integer.parseInt(total);

            // create a new item
            CalculationItem newItem = new CalculationItem(itemId, userInputLong, status);
            newItem.isPrime = prime; // todo setter
            newItem.progress = progressInt;
            newItem.total_progress = totalProgressInt;

            if (rootsRepr.equals("number is prime") || (rootsRepr.equals("calculating roots.."))) {
                newItem.roots = null;
            }
            else{
                String[] splitRoots = rootsRepr.split("!");
                String firstRoot = splitRoots[0];
                String secondRoot = splitRoots[1];

                // parse string to long
                long firstRootLong = 0;
                try {
                    firstRootLong = Long.parseLong(firstRoot);
                }
                catch (NumberFormatException e){
                    Log.e("error", "could not convert String to Long");
                    return null;
                }
                // parse string to long
                long secondRootLong = 0;
                try {
                    secondRootLong = Long.parseLong(secondRoot);
                }
                catch (NumberFormatException e){
                    Log.e("error", "could not convert String to Long");
                    return null;
                }
                Pair<Long, Long> rootsPair = new Pair<>(firstRootLong, secondRootLong);
                newItem.roots = rootsPair;
            }

            return newItem;
        } catch (Exception e) {
            System.out.println("exception: " + e.toString() + "input: " + stringRepr);
            return null;
        }
    } // end of stringToCalculationItem function
}
