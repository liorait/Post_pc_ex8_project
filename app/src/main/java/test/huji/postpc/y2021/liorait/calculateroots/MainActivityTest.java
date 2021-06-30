package test.huji.postpc.y2021.liorait.calculateroots;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;
import huji.postpc.y2021.liorait.calculateroots.CalculationItem;
import androidx.annotation.RequiresApi;

public class MainActivityTest {

        @Before
        public void setup(){
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Test
        public void when_adding_new_calculation_item_the_number_is_saved_correctly() {

            String id = UUID.randomUUID().toString();
            long number = 1;
            CalculationItem item = new CalculationItem(id, number, "new");
            Assert.assertEquals(item.getNumber(), number);
        }

        @Test
        public void when_adding_new_item_total_progress_is_100() {
            String id = UUID.randomUUID().toString();
            long number = 1;
            CalculationItem item = new CalculationItem(id, number, "new");
            Assert.assertEquals(item.getTotalProgress(), 100);
        }

        @Test
        public void when_adding_new_item_roots_is_null() {
            String id = UUID.randomUUID().toString();
            long number = 1;
            CalculationItem item = new CalculationItem(id, number, "new");
            Assert.assertNull(item.getRoots());
        }
    }
