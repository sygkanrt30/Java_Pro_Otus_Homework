package ru.otus.homework.client;

public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws InterruptedException {
        var client = new NumberClient(SERVER_HOST, SERVER_PORT);
        try {
            client.getNumbers(0, 30);
        } finally {
            client.shutdown();
        }
    }
}
