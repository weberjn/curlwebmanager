package de.jwi.curlmgr.process;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.concurrent.Callable;

public class CollectingStdOutConsumer implements Callable<Object>
{
	private InputStream inputStream;
	private StringWriter sw;

	public CollectingStdOutConsumer(InputStream inputStream, StringWriter sw)
	{
		super();
		this.inputStream = inputStream;
		this.sw = sw;
	}

	@Override
	public Object call() throws Exception
	{
		System.out.println("StdOutConsumer call");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String line = null;
		
		while ((line = br.readLine()) != null)
		{
			sw.write(line);
			sw.write("\n");
		}
		
		sw.close();
		return null;
	}

}
