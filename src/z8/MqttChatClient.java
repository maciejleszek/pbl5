package z8;

import org.eclipse.paho.client.mqttv3.*;

import java.util.Scanner;
import java.util.UUID;

public class MqttChatClient {

    MqttChatClient() throws MqttException, InterruptedException {
        String broker = "tcp://broker.emqx.io:1883";
        System.out.println("Broker: " + broker);

        String publisherId = UUID.randomUUID().toString();
        System.out.println("My ID: " + publisherId);

        IMqttClient publisher = new MqttClient(broker, publisherId);
        System.out.println("Publisher: " + publisher);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        System.out.println("Connect options: " + options);

        publisher.connect(options);
        if (!publisher.isConnected()) {
            System.out.println("NOT Connected!");
            publisher.close();
            return;
        }
        System.out.println("Connected!");

        String topic = "Czat";

        publisher.subscribe(topic, new IMqttMessageListener() {
            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                System.out.println(mqttMessage.toString());
            }
        });

        Scanner scanner = new Scanner(System.in);
        System.out.print("Podaj nick: ");
        String nick = scanner.nextLine();
        publisher.publish(topic, new MqttMessage(("SYSTEM: Dołączył użytkownik " + nick + ".").getBytes()));

        while (true) {
            String msg = scanner.nextLine();
            if (msg.equals("/exit")) {
                break;
            }
            msg = "[" + nick + "]: " + msg;
            MqttMessage mqttMessage = new MqttMessage(msg.getBytes());
            publisher.publish(topic, mqttMessage);
        }

        publisher.publish(topic, new MqttMessage(("SYSTEM: Użytkownik " + nick + " opuścił czat.").getBytes()));
        publisher.disconnect();
        publisher.close();
    }

    public static void main(String[] args) throws MqttException, InterruptedException {
        new MqttChatClient();
    }
}
