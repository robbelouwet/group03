package app.utils;

public interface IConsoleReader {
    String ask(String str);
    void println(String l);
    void print(String s);
    void printf(String format, Object obj);
}
