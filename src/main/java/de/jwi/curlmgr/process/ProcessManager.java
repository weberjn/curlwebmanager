package de.jwi.curlmgr.process;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.rice.cs.util.ArgumentTokenizer;

public class ProcessManager
{
	private ExecutorService executors = Executors.newCachedThreadPool();

	ConcurrentLinkedQueue<ProcessExecutor> processes = new ConcurrentLinkedQueue<>();

	public void shutdown()
	{
		executors.shutdown();
	}
	
	public Collection<ProcessExecutor> getProcessExecutors()
	{
		return processes;
	}

	public void killProcessByID(String id) throws Exception
	{
		for (ProcessExecutor process : processes)
		{
			if (process.getId().equals(id))
			{
				process.destroy();
			}
		}
	}

	public void removeAllProcesses()
	{
		for (ProcessExecutor process : processes)
		{
			if (process.future.isDone())
			{
				processes.remove(process);
			}
		}
	}

	public void removeProcessByID(String id, boolean deleteOutputFile)
	{
		for (ProcessExecutor process : processes)
		{
			if (process.getId().equals(id))
			{
				if (process.future.isDone())
				{
					processes.remove(process);
					if (deleteOutputFile)
					{
						deleteOutputFile(process);
					}
				}
			}
		}
	}

	private void deleteOutputFile(ProcessExecutor processExecutor)
	{
		File f = new File(processExecutor.directory, processExecutor.outputFilename);
		
		f.delete();
		
		f = new File(processExecutor.directory, processExecutor.outputFilename + ".done");
		
		f.delete();
	}
	
	public void resubmitProcessByID(String id)
	{
		for (ProcessExecutor process : processes)
		{
			if (process.getId().equals(id))
			{
				if (process.future.isDone())
				{
					Future<Integer> future = executors.submit(process);
					process.future = future;
				}
			}
		}
	}

	public void removeDoneProcesses()
	{
		List<ProcessExecutor> doneProcesses = new ArrayList<>(processes.size());
		Iterator<ProcessExecutor> it = processes.iterator();
		while (it.hasNext())
		{
			ProcessExecutor p = it.next();
			if (p.future.isDone())
			{
				doneProcesses.add(p);
			}
		}
		processes.removeAll(doneProcesses);
	}

	public void runCommand(File directory, String commandLine)
	{
		// http://stackoverflow.com/questions/3259143/split-a-string-containing-command-line-parameters-into-a-string-in-java

		commandLine = commandLine.replace('"', '\'');
		
		
		if (commandLine.startsWith("http://") || commandLine.startsWith("https://")) 
		{
			commandLine = "curl -L -O " + commandLine;
		}

		
		List<String> tokens = ArgumentTokenizer.tokenize(commandLine);

		String[] args = new String[tokens.size()];

		args = tokens.toArray(args);

		runCommand(commandLine, directory, args);
	}

	public void runCommand(String commandLine, File directory, String[] args)
	{
		// prevent double invocations
		
		for (ProcessExecutor process : processes)
		{
			if (process.getCommandLine().equals(commandLine))
			{
				return;
			}
		}
		
		ProcessExecutor processExecutor = new ProcessExecutor(commandLine, args, directory, null);
		Future<Integer> future = executors.submit(processExecutor);

		processExecutor.future = future;

		if (!args[0].equals("curl"))
		{
			return;
		}

		processes.add(processExecutor);
	}

}
