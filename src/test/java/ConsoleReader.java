import java.io.Reader;

// TODO: this is copy-pasted from stackoverflow, plagiarism???
public class ConsoleReader {
    private final Reader source;

    public ConsoleReader() {
        this(System.console().reader());
    }

    ConsoleReader(Reader source) {
        this.source = source;
    }
}
