package kafka

import (
	"fmt"
	"log"
	"os"

	kafka "github.com/confluentinc/confluent-kafka-go/v2/kafka"
)

func ListTopics() {
	adminClient, err := kafka.NewAdminClient(&kafka.ConfigMap{"bootstrap.servers": "kafka:29092"})
	if err != nil {
		log.Fatalf("Failed to create Admin client: %s", err)
	}
	defer adminClient.Close()

	metadata, err := adminClient.GetMetadata(nil, false, 10000)
	if err != nil {
		log.Fatalf("Failed to get metadata: %s", err)
	}

	for topic := range metadata.Topics {
		fmt.Println("Topic:", topic)
	}
}

func StartKafka() {
	p, err := kafka.NewProducer(&kafka.ConfigMap{"bootstrap.servers": "kafka:29092"})
	if err != nil {
		panic(err)
	}

	defer p.Close()

	// Delivery report handler for produced messages
	go func() {
		for e := range p.Events() {
			switch ev := e.(type) {
			case *kafka.Message:
				if ev.TopicPartition.Error != nil {
					fmt.Printf("Delivery failed: %v\n", ev.TopicPartition)
				} else {
					fmt.Printf("Delivered message to %v\n", ev.TopicPartition)
				}
			}
		}
	}()

	// Produce messages to topic (asynchronously)
	topic := "topic-cmd"
	for _, word := range []string{"Welcome", "to", "the", "Confluent", "Kafka", "Golang", "client"} {
		p.Produce(&kafka.Message{
			TopicPartition: kafka.TopicPartition{Topic: &topic, Partition: kafka.PartitionAny},
			Value:          []byte(word),
		}, nil)
	}

	// Wait for message deliveries before shutting down
	p.Flush(15 * 1000)

	consumer, err := kafka.NewConsumer(&kafka.ConfigMap{
		"bootstrap.servers": "kafka:29092",
		"group.id":          "gps-consumer-group",
		"auto.offset.reset": "smallest"})

	if err != nil {
		panic(err)
	}
	fmt.Println("consumer=", consumer)
	err = consumer.SubscribeTopics([]string{"topic-cmd"}, nil)
	if err != nil {
		panic(err)
	}

	// A signal handler or similar could be used to set this to false to break the loop.
	run := true

	for run == true {
		ev := consumer.Poll(100)
		switch e := ev.(type) {
		case *kafka.Message:
			// application-specific processing
		case kafka.Error:
			fmt.Fprintf(os.Stderr, "%% Error: %v\n", e)
			run = false
		default:
			fmt.Printf("Ignored %v\n", e)
		}
	}

	consumer.Close()

	consumer.Close()
}
