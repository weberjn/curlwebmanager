package curl;

import java.util.Arrays;
import java.util.Iterator;

import curl.ProcessManager.ManagedProcess;

public class ProcessManagerTest
{
	public static void main(String[] args) throws Exception
	{
		ProcessManager pm = new ProcessManager();
		pm.init();
		pm.runCommand("curl --fail http://releases.ubuntu.com/16.04.1/ubuntu-16.04.1-server-amd64.iso -o ubuntu-16.04.1-server-amd64.iso");

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
