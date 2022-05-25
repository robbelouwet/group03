package domain.scheduler;

import services.AlgorithmOptions;

public class FIFOBuilder implements AlgorithmBuilder {
    private FIFOSchedulingAlgorithm schedulingAlgorithm;

    @Override
    public void reset() {
        schedulingAlgorithm = new FIFOSchedulingAlgorithm();
    }

    @Override
    public void setSelectedOptions(AlgorithmOptions options) {
        // this does nothing
    }

    @Override
    public SchedulingAlgorithm getAlgorithm() {
        return schedulingAlgorithm;
    }
}
