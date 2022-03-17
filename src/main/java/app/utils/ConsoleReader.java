package app.utils;

import lombok.Getter;
import lombok.Setter;
import java.util.Scanner;

public class ConsoleReader implements IConsoleReader {
    @Getter
    @Setter // setter to inject a mock when testing
    // Setter should only be called from test package
    private static IConsoleReader instance = new ConsoleReader();

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String ask(String str) {
        ConsoleReader.getInstance().println(str);
        return scanner.nextLine();
    }

    @Override
    public void println(String l) {
        System.out.println(l);
    }

    @Override
    public void print(String s) {
        System.out.print(s);
    }

    @Override
    public void printf(String format, Object obj) {
        System.out.printf(format, obj);
    }

}
