package main

import (
	"crypto/tls"
	"crypto/x509"
	"fmt"
	"io/ioutil"
	"time"

	mqtt "github.com/eclipse/paho.mqtt.golang"
)

// Follow this tutorial to setup a thing: https://www.youtube.com/watch?v=qAe2zYqvQTQ
// Go to AWS IoT console >> MQTT test client to find the client ID and end point.
const END_POINT = "ssl://apv3187879vov-ats.iot.us-east-2.amazonaws.com:8883"
const CLIENT_ID = "iotconsole-e5e4ae47-d72b-424f-affd-457a4867236e"
const CERT_NUM = "3f268854908188dc666e99ab648c0df62b3bd95919d3f6a2f5accb0b98d7e69b"
const CMD_TOPIC = "topic/cmd"

var CERT_DIR = "./cert/"
var PATH_TO_ROOT = CERT_DIR + "AmazonRootCA1.pem"
var PATH_TO_KEY = CERT_DIR + CERT_NUM + "-private.pem.key"
var PATH_TO_CERT = CERT_DIR + CERT_NUM + "-certificate.pem.crt"

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

	// Publish a message
	token := client.Publish(CMD_TOPIC, 0, false, `{"cmd": "move backward"}`)
	token.Wait()
	print("Published!")

	// Give time for the message to be sent
	time.Sleep(1 * time.Second)

	// To check if message goes to AWS go to AWS IoT > MQTT test client.
}
