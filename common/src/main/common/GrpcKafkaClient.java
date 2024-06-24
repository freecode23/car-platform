package common;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class GrpcKafkaClient {
    private final KafkaServiceGrpc.KafkaServiceStub asyncStub;

    public GrpcKafkaClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
            .usePlaintext()
            .build();
        asyncStub = KafkaServiceGrpc.newStub(channel);
    }

    public void consumeMessages(Consumer<String> messageProcessor) {
        // Here we call the consume method on the gRPC server
        // This example assumes that the gRPC server is set up to continuously stream messages
        StreamObserver<KafkaMessage> responseObserver = new StreamObserver<KafkaMessage>() {
            @Override
            public void onNext(KafkaMessage message) {
                messageProcessor.accept(message.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("Stream completed");
            }
        };

        // Assuming the gRPC server has a method streamKafkaMessages that continuously sends Kafka messages
        asyncStub.streamKafkaMessages(KafkaRequest.newBuilder().build(), responseObserver);
    }
}
