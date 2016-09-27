package curl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ProcessManager
{
	private ExecutorService executors;

	ConcurrentLinkedQueue<ManagedProcess> managedProcesses = new ConcurrentLinkedQueue<ManagedProcess>();
	
	class ManagedProcess
	{
		ProcessExecutor processExecutor;
		Future<Integer> future;
	}

	public void init()
	{
		executors = Executors.newCachedThreadPool();
	}

	public void shutdown()
	{
		executors.shutdown();
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
	
	public void runCommand(String[] args)
	{
		ProcessExecutor processExecutor = new ProcessExecutor(args, null);
		Future<Integer> future = executors.submit(processExecutor);

		ManagedProcess managedProcess = new ManagedProcess();
		managedProcess.processExecutor = processExecutor;
		managedProcess.future = future;
		
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
				System.out.println(p.processExecutor.lastStdErrLine);
				System.out.println();
			}
			Thread.sleep(1000);
		}
		
		pm.removeDoneProcesses();
		
		pm.shutdown();
	}

}
