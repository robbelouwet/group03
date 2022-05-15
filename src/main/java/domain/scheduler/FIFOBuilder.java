package domain.scheduler;

import domain.car.options.Option;
import domain.car.options.OptionCategory;

import java.util.Map;

public class FIFOBuilder implements AlgorithmBuilder {
    private FIFOSchedulingAlgorithm schedulingAlgorithm;

    @Override
    public void reset() {
        schedulingAlgorithm = new FIFOSchedulingAlgorithm();
    }

    @Override
    public void setSelectedOptions(Map<OptionCategory, Option> options) {
        // this does nothing
    }

    @Override
    public SchedulingAlgorithm getAlgorithm() {
        return schedulingAlgorithm;
    }
}
