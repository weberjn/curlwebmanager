package curl;

import java.io.BufferedReader;
import java.io.FileWriter;
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
		System.out.println("StdErrConsumer call");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String line = null;

		FileWriter fw = new FileWriter("ERR_" + hashCode());

		while ((line = br.readLine()) != null)
		{
			fw.write(line);
			executor.setLastLine(line);
			fw.write("\n");
		}

		fw.close();
		return null;
	}
}
