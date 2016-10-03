package curl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
	private ExecutorService executors;

	ConcurrentLinkedQueue<ManagedProcess> managedProcesses = new ConcurrentLinkedQueue<ManagedProcess>();

	public class ManagedProcess
	{
		ProcessExecutor processExecutor;
		Future<Integer> future;
		
		public String getId()
		{
			return "" + hashCode();
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
			return processExecutor.outputFilename;
		}
		
		public String getReferer()
		{
			return processExecutor.referer;
		}
		
		public Date getStartDate()
		{
			return processExecutor.startedAt;
		}
		
		public String getLastLine()
		{
			return processExecutor.getLastLine();
		}
	}

	public void init()
	{
		executors = Executors.newCachedThreadPool();
	}

	public void shutdown()
	{
		executors.shutdown();
	}

	public Collection<ManagedProcess> getManagedProcesses()
	{
		return managedProcesses;
	}
	
	public void killProcessByID(String id)
	{
		for (ManagedProcess process : managedProcesses)
		{
			if (process.getId().equals(id))
			{
				if (!process.future.isDone())
				{
					process.future.cancel(true);
				}
			}
		}
	}
	
	public void removeProcessByID(String id)
	{
		for (ManagedProcess process : managedProcesses)
		{
			if (process.getId().equals(id))
			{
				if (process.future.isDone())
				{
					managedProcesses.remove(process);
				}
			}
		}
	}
	

	public void resubmitProcessByID(String id)
	{
		for (ManagedProcess process : managedProcesses)
		{
			if (process.getId().equals(id))
			{
				if (process.future.isDone())
				{
					Future<Integer> future = executors.submit(process.processExecutor);
					process.future = future;
				}
			}
		}
	}
	
	public void removeDoneProcesses()
	{
		List<ManagedProcess> doneProcesses = new ArrayList<ManagedProcess>(managedProcesses.size());
		Iterator<ManagedProcess> it = managedProcesses.iterator();
		while (it.hasNext())
		{
			ManagedProcess p = it.next();
			if (p.future.isDone())
			{
				doneProcesses.add(p);
			}
		}
		System.out.println("Processes done: " + doneProcesses.size());
		
		managedProcesses.removeAll(doneProcesses);
	}
	
	public void runCommand(File directory, String commandLine)
	{
		// http://stackoverflow.com/questions/3259143/split-a-string-containing-command-line-parameters-into-a-string-in-java
		
		List<String> tokens = ArgumentTokenizer.tokenize(commandLine);
		
		String[] args = new String[tokens.size()];
		
		args = tokens.toArray(args);
		
		runCommand(directory, args);
	}
	
	public void runCommand(File directory, String[] args)
	{
		ProcessExecutor processExecutor = new ProcessExecutor(args, directory, null);
		Future<Integer> future = executors.submit(processExecutor);

		ManagedProcess managedProcess = new ManagedProcess();
		managedProcess.processExecutor = processExecutor;
		managedProcess.future = future;
		
		if (!args[0].equals("curl"))
		{
			return;
		}
		
		managedProcesses.add(managedProcess);
	}
	


}
