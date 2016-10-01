package curl;

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

import dlm.util.CommandLineSplitter;

public class ProcessManager
{
	private ExecutorService executors;

	ConcurrentLinkedQueue<ManagedProcess> managedProcesses = new ConcurrentLinkedQueue<ManagedProcess>();

	private CommandLineSplitter commandLineSplitter;
	
	public class ManagedProcess
	{
		ProcessExecutor processExecutor;
		Future<Integer> future;
		
		public String getStatus()
		{
			return future.isDone() ? "finished" : "running";
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
		commandLineSplitter = new CommandLineSplitter();
	}

	public void shutdown()
	{
		executors.shutdown();
	}

	public Collection<ManagedProcess> getManagedProcesses()
	{
		return managedProcesses;
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
	
	public void runCommand(String commandLine)
	{
		String[] args = commandLineSplitter.splitCommandLine(commandLine);
		
		runCommand(args);
	}
	
	public void runCommand(String[] args)
	{
		ProcessExecutor processExecutor = new ProcessExecutor(args, null);
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
	
	public static void main(String[] args) throws Exception
	{
		String[] a1 = {"curl", "--fail", "http://ubuntu16/~weberjn/Fx.exe", "-o", "F.exe" };
		String[] a2 = {"curl", "--fail", "http://ubuntu16/~weberjn/F1.exe", "-o", "F1.exe" };
		
		ProcessManager pm = new ProcessManager();
		pm.init();
		pm.runCommand(a1);
		pm.runCommand(a2);
		
		boolean allDone = false;
		
		while (!allDone)
		{
			allDone = true;
			Iterator<ManagedProcess> it = pm.managedProcesses.iterator();
			while (it.hasNext())
			{
				ManagedProcess p = it.next();
				
				System.out.println(Arrays.asList(p.processExecutor.args));
				System.out.println("isdone: " + p.future.isDone());
				System.out.println("get: " + p.future.get());
				allDone &= p.future.isDone();
				System.out.println("alldone:" + allDone);
				System.out.println(p.processExecutor.getLastLine());
				System.out.println();
			}
			Thread.sleep(1000);
		}
		
		pm.removeDoneProcesses();
		
		pm.shutdown();
	}

}
