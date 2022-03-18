package app.utils;

/**
 * An interface that makes it possible to mock interacting with the console
 */
public interface IConsoleReader {
    /**
     * Ask a question to the console and get a response
     * @param str The question
     * @return the value the user typed in
     */
    String ask(String str);

    /**
     * Print a line and add a newline at the add
     * @param l the string to print
     */
    void println(String l);

    /**
     * Print without a newline
     * @param s the string to print
     */
    void print(String s);

    /**
     * Formatted print
     * @param format the format string
     * @param obj the object to format in the string
     */
    void printf(String format, Object obj);
}
