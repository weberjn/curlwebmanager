package de.jwi.curlmgr.process;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

public class StdErrConsumer implements Callable<Object>
{
	private InputStream inputStream;
	private ProcessExecutor executor;

	public StdErrConsumer(InputStream inputStream, ProcessExecutor executor)
	{
		super();
		this.inputStream = inputStream;
		this.executor = executor;
	}

	@Override
	public Object call() throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String line = null;

		while ((line = br.readLine()) != null)
		{
			executor.setLastLine(line);
		}

		return null;
	}
}
