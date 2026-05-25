package client.input;

import java.util.Scanner;

public class ConsoleReader {
    private Scanner scanner;

    public ConsoleReader() {
        this.scanner = new Scanner(System.in);
    }

    public ConsoleReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readLine() {
        return scanner.nextLine().trim();
    }

    public void print(String message) {
        System.out.print(message);
    }

    public void println(String message) {
        System.out.println(message);
    }
}