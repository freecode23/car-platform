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
const CLIENT_ID = "MacBook"
const CERT_NUM = "f276f24e1c2349d00ac57437d72620740582856ed359a4b64d1f17f9a88b7063"
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
	token := client.Publish(CMD_TOPIC, 0, false, `{"message": "Yo from GO"}`)
	token.Wait()
	print("Published!")

	// Give time for the message to be sent
	time.Sleep(1 * time.Second)

	// To check if message goes to AWS go to AWS IoT > MQTT test client.
}
