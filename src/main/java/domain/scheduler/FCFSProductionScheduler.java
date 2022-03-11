package domain.scheduler;

import domain.assembly.AssemblyProcess;
import domain.car.CarOrder;

// package-private!
class FCFSProductionScheduler extends ProductionScheduler {
    @Override
    public void addOrder(CarOrder order) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AssemblyProcess getNextProcess() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateSchedule(AssemblyProcess process) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProductionScheduler clone() {
        return new FCFSProductionScheduler();
    }
}
