package ru.otus.homework.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import ru.otus.homework.NumberRequest;
import ru.otus.homework.NumberResponse;
import ru.otus.homework.NumberStreamServiceGrpc;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NumberServer {
    private final int port;
    private final Server server;

    public NumberServer(int port) {
        this.port = port;
        this.server = ServerBuilder.forPort(port)
                .addService(new NumberStreamServiceImpl())
                .build();
    }

    public Server server() {
        return server;
    }

    public void start() throws IOException {
        server.start();
        log.info("Server started, listening on {}", port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.error("Shutting down gRPC server");
            try {
                this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }));
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private static class NumberStreamServiceImpl extends NumberStreamServiceGrpc.NumberStreamServiceImplBase {
        @Override
        public void streamNumbers(NumberRequest request, StreamObserver<NumberResponse> responseObserver) {
            int firstValue = request.getFirstValue();
            int lastValue = request.getLastValue();
            try {
                for (int i = firstValue + 1; i <= lastValue; i++) {
                    TimeUnit.SECONDS.sleep(2);
                    log.info("Current value: {}", i);
                    var response = NumberResponse.newBuilder()
                            .setValue(i)
                            .build();
                    responseObserver.onNext(response);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                responseObserver.onCompleted();
            }
        }
    }
}