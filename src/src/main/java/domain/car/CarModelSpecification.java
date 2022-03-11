package domain.car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarModelSpecification {
    private final Map<String, List<String>> options;

    public CarModelSpecification(Map<String, List<String>> options) {
        this.options = copyOptions(options);
    }

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
