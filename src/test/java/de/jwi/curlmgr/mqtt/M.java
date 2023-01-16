package de.jwi.curlmgr.mqtt;

public class M
{

	public static void main(String[] args)
	{
		String t = "{\"some\": \"json\"}";
		
		Object[] messageArgs = { "json", "words"};
		String message = String.format(t, messageArgs);

		System.out.println(message);
		
		t = "{\"some\": \"%s text\"}";
		message = String.format(t, messageArgs);

		System.out.println(message);
		
	}

}
