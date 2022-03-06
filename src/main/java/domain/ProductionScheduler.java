package domain;

public class ProductionScheduler {

    public void addOrder(CarOrder order){
        throw new UnsupportedOperationException();
    }

    public AssemblyProcess getNextProcess(){
        throw new UnsupportedOperationException();
    }

    private void updateSchedule(AssemblyProcess process){
        throw new UnsupportedOperationException();
    }

    @Override
    public ProductionScheduler clone() {
        return new ProductionScheduler();
    }
}
