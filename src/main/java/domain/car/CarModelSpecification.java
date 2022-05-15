package domain.car;

import domain.car.options.Option;
import domain.car.options.OptionCategory;
import domain.car.options.OptionSelector;

import java.util.*;

/**
 * An immutable object that represents the selectedOptions you can select for a carmodel
 */
public class CarModelSpecification {
    private final Map<OptionCategory, List<Option>> options;

    /**
     * @param options a map that maps the name of an option to the values you can select
     */
    public CarModelSpecification(Map<OptionCategory, List<Option>> options) {
        this.options = copyOptions(options);
    }

    /**
     * Get a helper class which validates selected selectedOptions for this specification
     */
    public OptionSelector getOptionSelector() {
        return new OptionSelector(copyOptions(options));
    }

    private static Map<OptionCategory, List<Option>> copyOptions(Map<OptionCategory, List<Option>> options) {
        Map<OptionCategory, List<Option>> copy = new LinkedHashMap<>();
        for (var key : options.keySet()) {
            copy.put(key.copy(), new ArrayList<>(options.get(key)));  // Option category rule is mutable, so should be copied as well
        }
        return copy;
    }

    public boolean isValidInputData(Map<OptionCategory, Option> selections) {
        var selector = getOptionSelector();
        for (var cat : selections.entrySet()) {
            if (!selector.selectOption(cat.getValue())) return false;
        }
        return selector.getNotSelectedCategories().size() == 0;
    }
}
