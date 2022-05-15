package services;

import domain.car.options.Option;
import domain.car.options.OptionCategory;

import java.util.Map;

/**
 * This record has the function of being open for change of possible new algorithm implementations.
 * Whenever a new scheduling algorithm is added and has new requirements, a new field can be introduced here so that we don't need to extend our
 * parameters of the main flow in Controller <-> Manager. The manager can simply unpack the fields he needs to build a scheduling algorithm
 *
 * @param selectedOptions a Map of OptionCategory as key and an Option as value. This serves as the chosen options to prioritize the orders for the specification batch algorithm.
 */
public record AlgorithmOptions(Map<OptionCategory, Option> selectedOptions) {
}
