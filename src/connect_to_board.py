import serial
import time
import datetime
import re

# Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
# SPDX-License-Identifier: Apache-2.0.

from awscrt import mqtt, http
from awsiot import mqtt_connection_builder
import sys
import threading
import time
import json
from utils.command_line_utils import CommandLineUtils

# This sample uses the Message Broker for AWS IoT to send and receive messages
# through an MQTT connection. On startup, the device connects to the server,
# subscribes to a topic, and begins publishing messages to that topic.
# The device should receive those same messages back from the message broker,
# since it is subscribed to that same topic.

# cmdData is the arguments/input from the command line placed into a single struct for
# use in this sample. This handles all of the command line parsing, validating, etc.
# See the Utils/CommandLineUtils for more information.
cmdData = CommandLineUtils.parse_sample_input_pubsub()

ENDPOINT  = "a13ssfxeh62jku-ats.iot.eu-central-1.amazonaws.com"
CA_FILE   = "root-CA.crt"
CERT      = "stm.cert.pem"
KEY       = "stm.private.key"
CLIENT_ID = "basicPubSub"
TOPIC     = "sdk/test/python"
COUNT     = 0
BOARD_ID  = 1

received_count = 0
received_all_event = threading.Event()

# Callback when connection is accidentally lost.
def on_connection_interrupted(connection, error, **kwargs):
    print("Connection interrupted. error: {}".format(error))


# Callback when an interrupted connection is re-established.
def on_connection_resumed(connection, return_code, session_present, **kwargs):
    print("Connection resumed. return_code: {} session_present: {}".format(return_code, session_present))

    if return_code == mqtt.ConnectReturnCode.ACCEPTED and not session_present:
        print("Session did not persist. Resubscribing to existing topics...")
        resubscribe_future, _ = connection.resubscribe_existing_topics()

        # Cannot synchronously wait for resubscribe result because we're on the connection's event-loop thread,
        # evaluate result with a callback instead.
        resubscribe_future.add_done_callback(on_resubscribe_complete)


def on_resubscribe_complete(resubscribe_future):
    resubscribe_results = resubscribe_future.result()
    print("Resubscribe results: {}".format(resubscribe_results))

    for topic, qos in resubscribe_results['topics']:
        if qos is None:
            sys.exit("Server rejected resubscribe to topic: {}".format(topic))


# Callback when the subscribed topic receives a message
def on_message_received(topic, payload, dup, qos, retain, **kwargs):
    print("Received message from topic '{}': {}".format(topic, payload))
    global received_count
    received_count += 1
    # if received_count == cmdData.input_count:
    if received_count == COUNT:
        received_all_event.set()

# Callback when the connection successfully connects
def on_connection_success(connection, callback_data):
    assert isinstance(callback_data, mqtt.OnConnectionSuccessData)
    print("Connection Successful with return code: {} session present: {}".format(callback_data.return_code, callback_data.session_present))

# Callback when a connection attempt fails
def on_connection_failure(connection, callback_data):
    assert isinstance(callback_data, mqtt.OnConnectionFailureData)
    print("Connection failed with error code: {}".format(callback_data.error))

# Callback when a connection has been disconnected or shutdown successfully
def on_connection_closed(connection, callback_data):
    print("Connection closed")

TEMPERATURE_TOPIC = "read/" + str(BOARD_ID) + "/temperature"

def get_topic(input_str: str):
    if input_str.find("Current temp:") != -1:
        return TEMPERATURE_TOPIC
    return None

ser = serial.Serial(
    port="COM3",
    baudrate=115200
)

# print("connected to: " + ser.portstr)

input_str = ""
current_topic = ""
busy = False
time_now = datetime.datetime.now()
time_busy = time_now

def publish_mqtt(client, msg, topic):
    message_json = json.dumps(msg)
    # print(f"Publishing message {message_json} to topic {topic} on broker {ENDPOINT}...")
    client.publish(
        topic=topic,
        payload=message_json,
        qos=mqtt.QoS.AT_LEAST_ONCE)

if __name__ == '__main__':
    # Create the proxy options if the data is present in cmdData
    proxy_options = None
    if cmdData.input_proxy_host is not None and cmdData.input_proxy_port != 0:
        proxy_options = http.HttpProxyOptions(
            host_name=cmdData.input_proxy_host,
            port=cmdData.input_proxy_port)

    # Create a MQTT connection from the command line data
    mqtt_connection = mqtt_connection_builder.mtls_from_path(
        # endpoint=cmdData.input_endpoint,
        endpoint=ENDPOINT,
        port=cmdData.input_port,
        # cert_filepath=cmdData.input_cert,
        cert_filepath=CERT,
        # pri_key_filepath=cmdData.input_key,
        pri_key_filepath=KEY,
        # ca_filepath=cmdData.input_ca,
        ca_filepath=CA_FILE,
        on_connection_interrupted=on_connection_interrupted,
        on_connection_resumed=on_connection_resumed,
        # client_id=cmdData.input_clientId,
        client_id=CLIENT_ID,
        clean_session=False,
        keep_alive_secs=30,
        http_proxy_options=proxy_options,
        on_connection_success=on_connection_success,
        on_connection_failure=on_connection_failure,
        on_connection_closed=on_connection_closed)

    if not cmdData.input_is_ci:
        print(f"Connecting to {cmdData.input_endpoint} with client ID '{cmdData.input_clientId}'...")
    else:
        print("Connecting to endpoint with client ID")
    connect_future = mqtt_connection.connect()

    # Future.result() waits until a result is available
    connect_future.result()
    print("Connected!")

    message_count = COUNT
    message_topic = TOPIC
    message_string = cmdData.input_message

    # Subscribe
    # print("Subscribing to topic '{}'...".format(message_topic))
    # subscribe_future, packet_id = mqtt_connection.subscribe(
    #     topic=message_topic,
    #     qos=mqtt.QoS.AT_LEAST_ONCE,
    #     callback=on_message_received)

    # subscribe_result = subscribe_future.result()
    # print("Subscribed with {}".format(str(subscribe_result['qos'])))

    regex_match_floats = r"[-+]?(?:\d*\.*\d+)"

    time.sleep(0.5)

    while True:
        try:
            time_now = datetime.datetime.now()
            if time_now < time_busy:
                time.sleep(0.5)
            else:
                busy = False
                time.sleep(0.1)
            lines = list()
            lines_filtered = list()
            if ser.in_waiting > 0:
                lines = ser.read(ser.in_waiting).decode()
                lines_filtered = lines.split("\n")
            
            # print(lines_filtered)

            for line in lines_filtered:
                line = line.strip()
                if line != ">" and line != input_str and line != "":
                    print(f"{line}")
                    # print(line.encode())
                    current_topic = get_topic(line)
                    if current_topic:
                        # print(current_topic)
                        extracted_value = float(re.findall(regex_match_floats, line)[0])
                        publish_mqtt(mqtt_connection, {"temp": extracted_value}, TOPIC)
            
            if not busy:
                print("> ", end="")
                input_str = input().strip()
                input_str_parts = input_str.split(" ")
                ser.write((input_str+"\n").encode())
                if input_str_parts[0] == "update_temp" and len(input_str_parts) == 3:
                    busy = True
                    time_busy = datetime.datetime.now() + datetime.timedelta(milliseconds=int(input_str_parts[2]))
            if not busy:
                time.sleep(0.1)
        except KeyboardInterrupt:
            break

    print("Disconnecting...")
    disconnect_future = mqtt_connection.disconnect()
    disconnect_future.result()
    print("Disconnected!")

    ser.close()