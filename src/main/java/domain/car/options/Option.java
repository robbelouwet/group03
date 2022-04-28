package domain.car.options;

public record Option(OptionCategory category, String name) {
    @Override
    public String toString() {
        return name;
    }
}
