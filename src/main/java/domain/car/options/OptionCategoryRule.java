package domain.car.options;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An OptionCategoryRule is a rule that can be applied to a category.
 */
public class OptionCategoryRule {
    private final Map<Option, Map<OptionCategory, List<Option>>> availableReplacements;

    /**
     * @param availableReplacements A mapping from an option to a mapping from categories to their new available selections after selecting the option
     */
    public OptionCategoryRule(Map<Option, Map<OptionCategory, List<Option>>> availableReplacements) {
        this.availableReplacements = availableReplacements;
    }

    /**
     * When an option is selected for the category that contains this rule, call this method.
     * It filters the available options on other categories based on the selected option.
     *
     * @param option           The selected option
     * @param availableOptions All options that could be selected in each category before selecting this option
     * @return All options that can be selected in each category after selecting this option
     */
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
