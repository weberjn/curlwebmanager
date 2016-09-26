package curl;

import java.io.InputStream;
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

	AtomicReference<String> lastStdErrLine = new AtomicReference<String>(); 
	
	Date startedAt;

	private ExecutorService executorService;

	private Process process;
	
	public ProcessExecutor(String[] args)
	{
		super();
		this.args = args;
	}

	public void setLastLine(String s)
	{
		lastStdErrLine.set(s);	
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

		process = pb.start();

		System.out.println("started");
		
		InputStream inputStream = process.getInputStream();
		InputStream errorStream = process.getErrorStream();

		
		StdOutConsumer stdOutConsumer = new StdOutConsumer(inputStream);
		
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
		ProcessExecutor pe = new ProcessExecutor(args);
		pe.call();
	}

	
}
