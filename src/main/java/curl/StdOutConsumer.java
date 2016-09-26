package curl;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

public class StdOutConsumer implements Callable<Object>
{
	private InputStream inputStream;

	public StdOutConsumer(InputStream inputStream)
	{
		super();
		this.inputStream = inputStream;
	}

	@Override
	public Object call() throws Exception
	{
		System.out.println("StdOutConsumer call");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String line = null;
		
		FileWriter fw = new FileWriter("OUT");
		
		while ((line = br.readLine()) != null)
		{
			fw.write(line);
			fw.write("\n");
		}
		
		fw.close();
		return null;
	}

}
