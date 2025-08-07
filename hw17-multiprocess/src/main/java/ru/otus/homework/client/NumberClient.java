package ru.otus.homework.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;
import ru.otus.homework.NumberRequest;
import ru.otus.homework.NumberResponse;
import ru.otus.homework.NumberStreamServiceGrpc;

@Slf4j
public class NumberClient {
    private final ManagedChannel channel;
    private final NumberStreamServiceGrpc.NumberStreamServiceStub asyncStub;
    private final AtomicInteger lastServerNumber = new AtomicInteger(0);

    public NumberClient(String host, int port) {
        this.channel =
                ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.asyncStub = NumberStreamServiceGrpc.newStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void getNumbers(int firstValue, int lastValue) throws InterruptedException {
        final var finishLatch = new CountDownLatch(1);
        var responseObserver = new StreamObserver<NumberResponse>() {
            @Override
            public void onNext(NumberResponse response) {
                lastServerNumber.set(response.getValue());
                log.info("Received from server: {}", response.getValue());
            }

            @Override
            public void onError(Throwable t) {
                log.error("Error: {}", t.getMessage());
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                log.info("Server completed sending numbers");
                finishLatch.countDown();
            }
        };
        var request = NumberRequest.newBuilder()
                .setFirstValue(firstValue)
                .setLastValue(lastValue)
                .build();
        asyncStub.streamNumbers(request, responseObserver);
        int currentValue = 0;
        for (int i = 0; i <= 50; i++) {
            TimeUnit.SECONDS.sleep(1);
            currentValue += lastServerNumber.getAndSet(0) + 1;
            log.info("Current value: {}", currentValue);
        }
        finishLatch.await();
    }
}