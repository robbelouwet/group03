package domain.car.options;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class OptionCategoryRule {
    private final Map<Option, Map<OptionCategory, List<Option>>> availableReplacements;

    /**
     * @param availableReplacements A mapping from an option to a mapping from categories to their new available selections after selecting the option
     */
    public OptionCategoryRule(Map<Option, Map<OptionCategory, List<Option>>> availableReplacements) {
        this.availableReplacements = availableReplacements;
    }

    public Map<OptionCategory, List<Option>> selected(Option option, Map<OptionCategory, List<Option>> availableOptions) {
        var newAvailable = availableReplacements.get(option);
        if (newAvailable != null) {
            for (var cat : newAvailable.entrySet()) {
                // Filter the options we had available on the options which are available after selecting this option
                availableOptions.put(cat.getKey(), availableOptions.get(cat.getKey()).stream().filter(option1 -> cat.getValue().contains(option1)).collect(Collectors.toList()));
            }
        }
        return availableOptions;
    }
}
