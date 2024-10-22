package math;

import java.util.ArrayList;
import java.util.List;

public class Math {
    /**
     * Calculates the median on a list of Longs. Does not alter the original list
     *
     * @param items the list of longs
     * @return The median
     */
    public static float median(List<Long> items) {
        items = new ArrayList<>(items);
        if (items.size() == 0) return 0;
        items.sort(Long::compareTo);
        return items.size() % 2 == 1 ? items.get((int) (items.size() / 2L)) : ((float) items.get((int) (items.size() / 2L)) + items.get((int) (items.size() / 2L) - 1)) / 2;
    }

    /**
     * Calculates the average on a list of Longs. Does not alter the original list
     *
     * @param items the list of longs
     * @return The median
     */
    public static float average(List<Long> items) {
        if (items.size() == 0) return 0;
        return items.stream().mapToInt(Long::intValue).sum() / (float) items.size();
    }
}
