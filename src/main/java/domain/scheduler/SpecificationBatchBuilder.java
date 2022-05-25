package domain.scheduler;

import domain.car.options.Option;
import domain.car.options.OptionCategory;

import java.util.Map;

public class SpecificationBatchBuilder implements AlgorithmBuilder {
    private SpecificationBatchSchedulingAlgorithm schedulingAlgorithm;

    @Override
    public void reset() {
        this.schedulingAlgorithm = new SpecificationBatchSchedulingAlgorithm();
    }

    @Override
    public void setSelectedOptions(Map<OptionCategory, Option> options) {
        schedulingAlgorithm.setSelectedOptions(options);
    }

    @Override
    public SchedulingAlgorithm getAlgorithm() {
        return schedulingAlgorithm;
    }
}
