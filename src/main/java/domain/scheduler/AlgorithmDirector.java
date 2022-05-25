package domain.scheduler;

import services.AlgorithmOptions;

/**
 * This class defines the order of steps to construct an algorithm.
 * The director knows exactly how to construct an algorithm while the AlgorithmBuilder knows how to implement
 * those specific steps. Analogy to a movie Director and the Actors.
 * The reason for making this class is for separation of concerns. We don't want the logic of building an algorithm
 * inside the ProductionSchedulerManager since the logic of constructing an algorithm can still change in the future we want
 * to extract that bit of logic. Another reason is for reusability.
 */
public class AlgorithmDirector {
    /**
     *
     * @param builder an AlgorithmBuilder who knows exactly how to perform the specific building tasks to construct a scheduling algorithm
     * @param options wrapper class to store data/fields for algorithm construction.
     *                Almost all algorithms need different parameters.
     *                This enables us to extract all options to use them at construction
     *
     * @return SchedulingAlgorithm
     */
    public SchedulingAlgorithm buildAlgorithm(AlgorithmBuilder builder, AlgorithmOptions options){
        builder.reset();
        builder.setSelectedOptions(options);
        return builder.getAlgorithm();
    }
}
