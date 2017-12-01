package de.jwi.curlmgr.process;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
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

	String commandLine;
	
	private AtomicReference<String> lastStdErrLine = new AtomicReference<String>(); 
	
	Date startDate, endDate;
	
	String outputFilename;
	String referer;
	
	private ExecutorService executorService;
	private StringWriter stdoutStringWriter;

	private Process process;
	private File directory;

	
	public ProcessExecutor(String commandLine, String[] args, File directory, StringWriter stdout)
	{
		super();
		this.commandLine = commandLine;
		this.args = args;
		this.stdoutStringWriter = stdout;
		this.directory = directory;
		
		for (int i = 0; i < args.length; i++)
		{
			if ("-o".equals(args[i]) || "--output".equals(args[i]))
			{ 
				outputFilename = args[++i];
			}
			
			if ("--referer".equals(args[i]))
			{
				referer = args[++i];
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
	
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}
	
	public void destroy()
	{
		process.destroyForcibly();
	}
	
	@Override
	public Integer call() throws Exception
	{
		startDate = new Date();
		
		ProcessBuilder pb = new ProcessBuilder(args);
		pb.directory(directory);

		process = pb.start();
		
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
		
		for(Future<Object> future : futures){
		    Object o = future.get();
		}

		executorService.shutdown();
		
		endDate = new Date();
		
		return exitValue;
	}
	
	public static void main(String[] args) throws Exception
	{
		StringWriter sw = new StringWriter();
		ProcessExecutor pe = new ProcessExecutor("", args, new File("."), sw);
		pe.call();
	}

	
}
