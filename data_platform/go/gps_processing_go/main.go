package main

import (
	"fmt"

	kafka "github.com/freecode23/rc_car/gps_processing_go/kafka"
)

var Version string

func main() {
	fmt.Println("OK")
	fmt.Println("App kafka started, version:", Version)
	kafka.CreateKafkaConsumer()
}
