package services.assembly;

import lombok.Getter;

public abstract class AssemblyManager {

    @Getter
    // inject concrete instance
    private static final AssemblyManager instance = new DefaultAssemblyManager();

    public abstract void advance();
}
