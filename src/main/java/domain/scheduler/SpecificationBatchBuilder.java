package domain.scheduler;

import services.AlgorithmOptions;

public class SpecificationBatchBuilder implements AlgorithmBuilder {
    private SpecificationBatchSchedulingAlgorithm schedulingAlgorithm;

    @Override
    public void reset() {
        this.schedulingAlgorithm = new SpecificationBatchSchedulingAlgorithm();
    }

    @Override
    public void setSelectedOptions(AlgorithmOptions options) {
        schedulingAlgorithm.setSelectedOptions(options.selectedOptions());
    }

    @Override
    public SchedulingAlgorithm getAlgorithm() {
        return schedulingAlgorithm;
    }
}
