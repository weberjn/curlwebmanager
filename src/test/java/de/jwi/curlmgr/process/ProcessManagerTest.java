package de.jwi.curlmgr.process;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import de.jwi.curlmgr.process.ProcessManager;

public class ProcessManagerTest
{
	public static void main(String[] args) throws Exception
	{
		File f = new File(".");
		
		ProcessManager pm = new ProcessManager(null);
		
		pm.runCommand(f, "curl http://releases.ubuntu.com/16.04.1/ubuntu-16.04.1-server-amd64.iso -o ubuntu-16.04.1-server-amd64.iso");

		boolean allDone = false;

		while (!allDone)
		{
			allDone = true;
			Iterator<ProcessExecutor> it = pm.processes.iterator();
			while (it.hasNext())
			{
				ProcessExecutor p = it.next();

				System.out.println(Arrays.asList(p.args));
				System.out.println("isdone: " + p.future.isDone());
				System.out.println("get: " + p.future.get());
				allDone &= p.future.isDone();
				System.out.println("alldone:" + allDone);
				System.out.println(p.getLastLine());
				System.out.println();
			}
			Thread.sleep(1000);
		}

		pm.removeDoneProcesses();

		pm.shutdown();
	}
}
