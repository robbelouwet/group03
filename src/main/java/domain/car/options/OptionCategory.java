package domain.car.options;

import lombok.Getter;

import java.util.Objects;

public final class OptionCategory {
    @Getter
    private final String name;
    @Getter
    private OptionCategoryRule optionCategoryRule;

    public OptionCategory(String name) {
        this.name = name;
    }

    public void setOptionCategoryRule(OptionCategoryRule optionCategoryRule) {
        this.optionCategoryRule = optionCategoryRule;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (OptionCategory) obj;
        return Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
