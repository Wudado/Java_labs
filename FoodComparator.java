package bsu.rfe.java.group5.Lab1_Barsukevich.var12B;

import java.util.Comparator;

public class FoodComparator implements Comparator<Food> {
    public int compare(Food arg0, Food arg1) {
        if (arg0==null) return 1;
        if (arg1==null) return -1;
        return String.valueOf(Integer.MAX_VALUE - arg0.calculateCalories()).compareTo(String.valueOf(Integer.MAX_VALUE - arg1.calculateCalories()));
    }
}
