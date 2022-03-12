package domain.car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An immutable object that represents the options you can select for a carmodel
 */
public class CarModelSpecification {
    private final Map<String, List<String>> options;

    /**
     * @param options a map that maps the name of an option to the values you can select
     */
    public CarModelSpecification(Map<String, List<String>> options) {
        this.options = copyOptions(options);
    }

    /**
     * A map that maps the name of an option to the values you can select
     */
    public Map<String, List<String>> getOptions() {
        return copyOptions(options);
    }

    private static Map<String, List<String>> copyOptions(Map<String, List<String>> options) {
        Map<String, List<String>> copy = new HashMap<>();
        for (String key : options.keySet()) {
            copy.put(key, new ArrayList<>(options.get(key)));
        }
        return copy;
    }
}
