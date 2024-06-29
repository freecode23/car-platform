package kafka

import (
	"fmt"
	"log"
	"time"

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
	consumer, err := kafka.NewConsumer(&kafka.ConfigMap{
		"bootstrap.servers": "kafka:29092",
		"group.id":          "gps-consumer-group",
		"auto.offset.reset": "smallest"})

	if err != nil {
		panic(err)
	}
	fmt.Println("consumer=", consumer)
	err = consumer.SubscribeTopics([]string{"topic-sensor"}, nil)
	if err != nil {
		panic(err)
	}

	// A signal handler or similar could be used to set this to false to break the loop.
	run := true

	for run {
		msg, err := consumer.ReadMessage(10 * time.Second)
		fmt.Println("msg=", msg)
		fmt.Println("err=", err)
		if err == nil {
			fmt.Printf("Message on %s: %s\n", msg.TopicPartition, string(msg.Value))
		} else if !err.(kafka.Error).IsTimeout() {
			// The client will automatically try to recover from all errors.
			// Timeout is not considered an error because it is raised by
			// ReadMessage in absence of messages.
			fmt.Printf("Consumer error: %v (%v)\n", err, msg)
		}
	}

	consumer.Close()
}
