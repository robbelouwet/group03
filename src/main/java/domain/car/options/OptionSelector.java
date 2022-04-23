package domain.car.options;

import domain.car.CarModel;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A helper class for creating the option selection.
 * After selecting each option, it updates the available options on other categories
 */
public class OptionSelector {
    private Map<OptionCategory, List<Option>> options;
    private final Map<OptionCategory, Option> selectedOptions = new LinkedHashMap<>();

    public OptionSelector(Map<OptionCategory, List<Option>> options) {
        this.options = options;
    }

    /**
     * @param option The option to select for this category
     * @return null if the selected option is not a valid choice, otherwise the new options after selecting this option
     */
    private Map<OptionCategory, List<Option>> getNewOptions(Option option) {
        var category = option.category();
        Map<OptionCategory, List<Option>> newOptions;
        // There might be a rule associated with this category, which filters other options. We apply this first
        if (category.getOptionCategoryRule() != null) {
            newOptions = category.getOptionCategoryRule().selected(option, new LinkedHashMap<>(options));
            // If, after selecting this option, there is another category without valid entries, we can't select this option
            for (var cat : newOptions.entrySet()) {
                if (cat.getValue().isEmpty()) return null;
            }

        } else newOptions = options;

        // Check if we can select this option
        if (!newOptions.get(category).contains(option)) return null;
        // Check if our old selections are still valid
        for (var cat : selectedOptions.entrySet()) {
            if (!newOptions.get(cat.getKey()).contains(cat.getValue())) return null;
        }
        return newOptions;
    }

    /**
     * Select this option and set it in the selected options
     *
     * @param option The option to select for this category
     * @return true if this is a valid selection
     */
    public boolean selectOption(Option option) {
        var newOptions = getNewOptions(option);
        if (newOptions == null) return false;
        // Now, we know this was a valid selection
        options = newOptions;
        selectedOptions.put(option.category(), option);
        return true;
    }

    /**
     * @return The categories and their options for which you still need to select an option
     */
    public Map<OptionCategory, List<Option>> getNotSelectedCategories() {
        var copyCats = new LinkedHashMap<>(options);
        for (var cat : selectedOptions.entrySet()) {
            copyCats.remove(cat.getKey());
        }
        return copyCats;
    }

    /**
     * If you are finished, you can call this method to get the options that you selected
     * Throws an error if you have not selected an option in each category
     */
    public Map<OptionCategory, Option> getSelectedOptions() {
        if (options.size() != selectedOptions.size())
            throw new IllegalStateException("Can't get selected options if not all options are selected yet!");
        return new LinkedHashMap<>(selectedOptions);  // Return a copy of the selected options
    }
}
