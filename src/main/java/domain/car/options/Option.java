package domain.car.options;

import lombok.Getter;

public record Option(OptionCategory category, String name) {
    @Override
    public String toString() {
        return name;
    }
}
