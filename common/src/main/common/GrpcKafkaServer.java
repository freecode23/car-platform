// common/src/main/java/common/GrpcKafkaServer.java

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class GrpcKafkaServer {
    private final KafkaService kafkaService = new KafkaService();

    public static void main(String[] args) throws Exception {
        GrpcKafkaServer server = new GrpcKafkaServer();
        server.start();
    }

    private void start() throws Exception {
        Server server = ServerBuilder.forPort(50051)
            .addService(new KafkaServiceImpl(kafkaService))
            .build()
            .start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        server.awaitTermination();
    }

    static class KafkaServiceImpl extends KafkaServiceGrpc.KafkaServiceImplBase {
        private final KafkaService kafkaService;

        KafkaServiceImpl(KafkaService kafkaService) {
            this.kafkaService = kafkaService;
        }

        @Override
        public void createProducer(CreateProducerRequest req, StreamObserver<CreateProducerResponse> responseObserver) {
            kafkaService.setupProducer(req.getBootstrapServers());
            responseObserver.onNext(CreateProducerResponse.newBuilder().build());
            responseObserver.onCompleted();
        }

        @Override
        public void createConsumer(CreateConsumerRequest req, StreamObserver<CreateConsumerResponse> responseObserver) {
            kafkaService.setupConsumer(req.getBootstrapServers(), req.getGroupId(), req.getTopic());
            responseObserver.onNext(CreateConsumerResponse.newBuilder().build());
            responseObserver.onCompleted();
        }

        @Override
        public void consume(ConsumeRequest req, StreamObserver<KafkaMessage> responseObserver) {
           // consume command message and publish to mqtt.
            kafkaService.consume(message -> {
                // Build a KafkaMessage with the consumed message
                KafkaMessage kafkaMessage = KafkaMessage.newBuilder().setMessage(message).build();
              
                // Send the message to the client
                responseObserver.onNext(kafkaMessage);
            });
        }

        @Override
        public void sendMessage(SendMessageRequest req, StreamObserver<SendMessageResponse> responseObserver) {
            // Send the provided message to the specified Kafka topic
            kafkaService.send(req.getTopic(), req.getMessage());

            // Respond to the client that the message was sent
            responseObserver.onNext(SendMessageResponse.newBuilder().build());
            responseObserver.onCompleted();
        }
    }
}
