package de.jwi.curlmgr.mqtt;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.MqttException;

public class T
{

	public static void main(String[] args) throws MqttException, Exception
	{
		File f = new File ("src/main/webapp/WEB-INF/curlmgr.properties");
		FileInputStream fis = new FileInputStream(f);
		
		Properties p = new Properties();
		p.load(fis);
		fis.close();
		
		p.setProperty("mqtt.url", "tcp://pi");
		
		MQTTNotifier n = new MQTTNotifier(p);
		
		n.notify(true, "nofile.out", "noline");

		p.setProperty("mqtt.messageSuccess", "message with no params");
		
		n.notify(true, "nofile.out", "noline");
		
		p.setProperty("mqtt.messageSuccess", "{ message : %s}");
		
		n.notify(true, "nofile.out", "noline");

	}

}
