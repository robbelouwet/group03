package domain.scheduler;

import domain.car.options.Option;
import domain.car.options.OptionCategory;

import java.util.Map;

/**
 * --- BUILDER PATTERN ---
 * This interface lets us construct a scheduling algorithm step by step by using the same implementations.
 * Reason of existence -> New incoming scheduling algorithms could need different requirements as parameter options.
 * This way, the ProductionSchedulerManager doesn't even need to know how to build a specific algorithm anymore.
 * It's all abstracted and just needs to provide the data to the builder.
 */
public interface AlgorithmBuilder {
    /**
     * Start of the construction. Internal creation of the algorithm in the implementation classes of this interface.
     */
    void reset();

    /**
     * Sets the selected options for the algorithm.
     * (Currently used for the specification batch algorithm)
     *
     * @param options a Map that serves as the chosen options to prioritize the pending orders for the algorithm.
     */
    void setSelectedOptions(Map<OptionCategory, Option> options);

    /**
     * Construction of the algorithm has been completed, now we need the concrete implementation.
     *
     * @return specific constructed algorithm
     */
    SchedulingAlgorithm getAlgorithm();
}
