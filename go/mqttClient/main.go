package main
// go mod edit -replace github.com/IBM/sarama=github.com/Shopify/sarama@v1.43.2


import (
	"bufio"
	"crypto/tls"
	"crypto/x509"
	"fmt"
	"io/ioutil"
	"os"
	"strings"
	"sync"

	mqtt "github.com/eclipse/paho.mqtt.golang"
)

// Follow this tutorial to setup a thing: https://www.youtube.com/watch?v=qAe2zYqvQTQ
// Go to AWS IoT console >> MQTT test client to find the client ID and end point.
const END_POINT = "ssl://apv3187879vov-ats.iot.us-east-2.amazonaws.com:8883"
const CLIENT_ID = "MacBook"
const CERT_NUM = "f276f24e1c2349d00ac57437d72620740582856ed359a4b64d1f17f9a88b7063"
const CMD_TOPIC = "topic/cmd"
const SENSOR_TOPIC = "topic/sensor"

var CERT_DIR = "./cert/"
var PATH_TO_ROOT = CERT_DIR + "AmazonRootCA1.pem"
var PATH_TO_KEY = CERT_DIR + CERT_NUM + "-private.pem.key"
var PATH_TO_CERT = CERT_DIR + CERT_NUM + "-certificate.pem.crt"
var wg sync.WaitGroup

func main() {
	// Load AWS IoT Core Root CA
	cafile, err := ioutil.ReadFile(PATH_TO_ROOT)
	if err != nil {
		panic(err)
	}
	caCertPool := x509.NewCertPool()
	caCertPool.AppendCertsFromPEM(cafile)

	// Load client certificate
	cert, err := tls.LoadX509KeyPair(PATH_TO_CERT, PATH_TO_KEY)
	if err != nil {
		panic(err)
	}

	// Create TLS configuration
	tlsConfig := &tls.Config{
		RootCAs:      caCertPool,
		Certificates: []tls.Certificate{cert},
	}

	// Create MQTT client options
	opts := mqtt.NewClientOptions()
	opts.AddBroker(END_POINT)
	opts.SetClientID(CLIENT_ID) // Ensure this is unique within your AWS IoT account
	opts.SetTLSConfig(tlsConfig)

	// Create and start a client using the above ClientOptions
	client := mqtt.NewClient(opts)
	if token := client.Connect(); token.Wait() && token.Error() != nil {
		fmt.Println("Token error")
		panic(token.Error())
	}
	defer client.Disconnect(250)

	// Define the message handler when mqtt message arrives.
	messageHandler := func(client mqtt.Client, msg mqtt.Message) {
		fmt.Printf("Received message: %s\n", msg.Payload())
	}

	// Subscribe to MQTT topic routine.
	wg.Add(1)
	go func() {
		if token := client.Subscribe(SENSOR_TOPIC, 1, messageHandler); token.Wait() && token.Error() != nil {
			fmt.Fprintf(os.Stderr, "Subscribe error: %s\n", token.Error())
			os.Exit(1)
		}
		fmt.Println("Subscription successful")
	}()

	// Publish MQTT topic routine.
	wg.Add(1)
	reader := bufio.NewReader(os.Stdin)
	go func() {
		fmt.Print("Enter command ('w' for forward, 's' for backward, 'q' to quit): ")
		defer wg.Done()
		for {
			cmd, _ := reader.ReadString('\n')
			cmd = strings.TrimSpace(cmd)
			if cmd == "q" {
				return // exit go routine.
			}
			switch cmd {
			case "w":
				token := client.Publish(CMD_TOPIC, 0, false, `{"message": "forward"}`)
				token.Wait()
			case "s":
				token := client.Publish(CMD_TOPIC, 0, false, `{"message": "backward"}`)
				token.Wait()
			case "a":
				token := client.Publish(CMD_TOPIC, 0, false, `{"message": "left"}`)
				token.Wait()
			case "d":
				token := client.Publish(CMD_TOPIC, 0, false, `{"message": "right"}`)
				token.Wait()
			case "x":
				token := client.Publish(CMD_TOPIC, 0, false, `{"message": "stop"}`)
				token.Wait()
			default:
				fmt.Println("Unknown command")
			}
		}
	}()
	wg.Wait() // Wait for both goroutines to finish
}
