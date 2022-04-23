package domain.car.options;

import org.junit.jupiter.api.Test;
import persistence.CarCatalog;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class OptionSelectorTest {
    OptionCategory getCategory(Map<OptionCategory, List<Option>> remCats, String name) {
        for (var cat : remCats.keySet())
            if (cat.getName().equals(name)) return cat;
        return null;
    }

    @Test
    void checkRules() {
        // Create a selector for Model B
        var selector = (new CarCatalog()).getModels().get(1).getOptionSelector();
        var options = selector.getNotSelectedCategories();
        var spoilerCat = getCategory(options, "Spoiler");

        // At the start you should be able to select Low or no spoiler for Model B
        assertEquals(List.of("Low", "None"), options.get(spoilerCat).stream().map(Option::name).collect(Collectors.toList()));
        var bodyCat = getCategory(options, "Body");
        var sport = options.get(bodyCat).get(2);
        // Make sure this is actually the sport option
        assertEquals("Sport", sport.name());
        selector.selectOption(sport);

        // Now there should be less spoiler options
        var options2 = selector.getNotSelectedCategories();
        assertEquals(List.of("Low"), options2.get(spoilerCat).stream().map(Option::name).collect(Collectors.toList()));
    }
}