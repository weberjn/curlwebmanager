package de.jwi.curlmgr.mqtt;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTNotifier
{
	private MqttClientPersistence clientPersistence;

	private Properties properties;

	public MQTTNotifier(Properties properties)
	{
		this.properties = properties;
		clientPersistence = new MemoryPersistence();
	}

	public void notify(boolean success, String fileName, String LastLine) throws MqttException
	{
		String url = properties.getProperty("mqtt.url").strip();
		String clientId = properties.getProperty("mqtt.clientid").strip();
		MqttClient client = new MqttClient(url, clientId, clientPersistence);

		client.connect();

		String topic = success ? properties.getProperty("mqtt.topicSuccess") : properties.getProperty("mqtt.topicFailure");

		String template = success ? properties.getProperty("mqtt.messageSuccess") : properties.getProperty("mqtt.messageFailure");
		
		Object[] messageArgs = { fileName, LastLine };
		String message = String.format(template, messageArgs);

		MqttMessage mqttMessage = new MqttMessage(message.getBytes());
		
		client.publish(topic, mqttMessage);

		client.disconnect();
	}
}
