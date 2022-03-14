package app.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.Scanner;

public class ConsoleReader {
    @Getter
    @Setter // setter to inject a mock when testing
    private static ConsoleReader instance = new ConsoleReader();

    public String ask(String str) {
        System.out.println(str);
        return (new Scanner(System.in).nextLine());
    }
}
