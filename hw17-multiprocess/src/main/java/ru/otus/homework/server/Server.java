package ru.otus.homework.server;

import java.io.IOException;

public class Server {
    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        var server = new NumberServer(SERVER_PORT);
        server.start();
        server.server().awaitTermination();
    }
}
