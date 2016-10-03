package curl;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

public class ProcessExecutor implements Callable<Integer>
{
	String[] args;

	private AtomicReference<String> lastStdErrLine = new AtomicReference<String>(); 
	
	Date startedAt;
	String outputFilename;
	String referer;
	
	private ExecutorService executorService;
	private StringWriter stdoutStringWriter;

	private Process process;
	private File directory;

	
	public ProcessExecutor(String[] args, File directory, StringWriter stdout)
	{
		super();
		this.args = args;
		this.stdoutStringWriter = stdout;
		
		for (int i = 0; i < args.length; i++)
		{
			if ("-o".equals(args[i]))
			{
				outputFilename = args[++i];
			}
			
			if (args[i].startsWith("Referer"))
			{
				referer = args[i].substring("Referer".length() + 1);
			}
		}
	}

	public void setLastLine(String s)
	{
		lastStdErrLine.set(s);	
	}
	
	public String getLastLine()
	{
		return lastStdErrLine.get();	
	}
	
	public void destroy()
	{
		process.destroy();
	}
	
	@Override
	public Integer call() throws Exception
	{
		startedAt = new Date();
		
		ProcessBuilder pb = new ProcessBuilder(args);
		pb.directory(directory);

		process = pb.start();

		System.out.println("started");
		
		InputStream inputStream = process.getInputStream();
		InputStream errorStream = process.getErrorStream();

		
		StdOutConsumer stdOutConsumer = new StdOutConsumer(inputStream, stdoutStringWriter);
		
		StdErrConsumer stdErrConsumer = new StdErrConsumer(errorStream, this);	

		Set<Callable<Object>> callables = new HashSet<Callable<Object>>();	
		callables.add(stdOutConsumer);
		callables.add(stdErrConsumer);
		
		executorService = Executors.newFixedThreadPool(2);

		List<Future<Object>> futures = executorService.invokeAll(callables);

		int exitValue = process.waitFor();
		
		System.err.println(Arrays.asList(args) + " waitFor done " + exitValue);
		
		System.out.println("futures invoked");
		
		for(Future<Object> future : futures){
		    System.out.println("future.get = " + future.get());
		}

		executorService.shutdown();
		
		
		return exitValue;
	}
	
	public static void main(String[] args) throws Exception
	{
		StringWriter sw = new StringWriter();
		ProcessExecutor pe = new ProcessExecutor(args, new File("."), sw);
		pe.call();
	}

	
}
