package de.jwi.curlmgr.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	
	private Date startDate, endDate;
	
	private String outputFilename;
	private String referer;

	private ProcessManager processManager;
	
	private ExecutorService ioExecutorService;
	private StringWriter stdoutStringWriter;

	private Process process;
	private File directory;


	Future<Integer> future;

	
	public ProcessExecutor(ProcessManager processManager, String commandLine, String[] args, File directory, StringWriter stdout)
	{
		super();
		this.processManager = processManager; 
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

	public String getId()
	{
		return "" + hashCode();
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
	
	public String getStatus()
	{
		if (future.isCancelled())
		{
			return "canceled";
		}
		if (future.isDone())
		{
			return "done";
		}

		return "running";
	}

	public String getFilename()
	{
		return outputFilename;
	}
	
	public File getDirectory()
	{
		return directory;
	}

	public String getCommandLine()
	{
		return commandLine;
	}

	public String getReferer()
	{
		return referer;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}


	
	public boolean isSuccess()
	{
		boolean rc = getLastLine() != null && getLastLine().startsWith("100");
		return rc;
	}
	
	
	
	public void destroy() throws Exception
	{
		if (!process.isAlive())
		{
			return;
		}

		setEndDate(new Date());
		
		process.getOutputStream().close();
		
		process.destroy();
		
		if (process.isAlive())
		{
			process.destroyForcibly();
		}
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
		
		ioExecutorService = Executors.newFixedThreadPool(2);

		writeLog();
		
		List<Future<Object>> futures = ioExecutorService.invokeAll(callables);

		int exitValue = process.waitFor();
		
		for(Future<Object> future : futures){
		    Object o = future.get();
		}

		ioExecutorService.shutdown();
		
		endDate = new Date();

		writeLog();
		
		if (processManager != null)
		{
			processManager.notify(this);
		}
		
		return exitValue;
	}
	
	private void writeLog() throws IOException
	{
		String l = null;
		
		if (endDate != null)
		{
				l = String.format("%tF %<tT %tF %<tT %s %s %s",  startDate, endDate, 
				outputFilename, referer, getLastLine());
		}
		else // start
		{
				l = String.format("%tF %<tT %s %s",  startDate, 
						outputFilename, referer);
		}
		
		File f = new File(directory, outputFilename + ".started");
		
		if (endDate != null)
		{
			File f1 = new File(directory, outputFilename + ".done");
			f.renameTo(f1);
			f = f1;
		}

		BufferedWriter bw = new BufferedWriter(new FileWriter(f));

		bw.append(l);
		
		bw.newLine();
		
		bw.close();
	}

}
