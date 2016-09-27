package dlm;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import curl.ProcessExecutor;

public class ControllerServlet extends HttpServlet
{
	private static String version = "unknown";
	private static String builddate = "unknown";
	
	
	
	
	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		super.service(arg0, arg1);
	}



	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		putServerInfo(request);

		RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/WEB-INF/dlm.jsp");

		requestDispatcher.forward(request, response);
	}
	
	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String pathInfo = null;
		pathInfo = request.getPathInfo();
		
		String servletPath = request.getServletPath();
		
		System.out.println(servletPath);
		
		System.out.println(pathInfo);
		
		String action = request.getParameter("action");

		if ("Submit".equals(action))
		{
			String curl = request.getParameter("curl");
			System.out.println("submit: " + curl);
		}
		
		if ("curl -V".equals(action))
		{
			try
			{
				doTest(request);
			} catch (Exception e)
			{
				throw new ServletException(e);
			}
		}
		
		putServerInfo(request);
		
		RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/WEB-INF/dlm.jsp");

		requestDispatcher.forward(request, response);
	}

	private void doTest(HttpServletRequest request) throws Exception
	{
		String[] args = { "curl", "-V"};
		
		StringWriter sw = new StringWriter();
		
		ProcessExecutor processExecutor = new ProcessExecutor(args, sw);
		Integer status = processExecutor.call();
		
		request.setAttribute("curlV", sw.toString());
	}
	
	private void putServerInfo(HttpServletRequest request)
	{
		String contextPath = request.getContextPath();
		request.setAttribute("contextPath", contextPath);
		
		String serverInfo = getServletContext().getServerInfo();
		request.setAttribute("serverInfo", serverInfo);
		
		String javaVersion = System.getProperty("java.version");
		request.setAttribute("javaVersion", javaVersion);

		String javaVendor = System.getProperty("java.vendor");
		request.setAttribute("javaVendor", javaVendor);
		
		String computerName = getComputerName();
		request.setAttribute("computerName", computerName);
		
		Runtime runtime = Runtime.getRuntime();
		long totalMemory = runtime.totalMemory(); // current heap allocated to the VM process
		long freeMemory = runtime.freeMemory(); // out of the current heap, how much is free
		long maxMemory = runtime.maxMemory(); // Max heap VM can use e.g. Xmx setting
		
		double m = 1024 * 1024;

	    DecimalFormat dec = new DecimalFormat("0.00");
		
		request.setAttribute("totalMemory", dec.format(totalMemory / m));
		request.setAttribute("freeMemory", dec.format(freeMemory / m));
		request.setAttribute("maxMemory", dec.format(maxMemory / m));
		
		long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(startTime);
		Date time = c.getTime();
		request.setAttribute("startTime", time.toString());
		
		try
		{
			InetAddress address = InetAddress.getByName(computerName); 
			String hostAddress = address.getHostAddress();
			
			request.setAttribute("hostAddress", hostAddress);
		} catch (UnknownHostException e)
		{
			getServletContext().log(computerName, e);
		}
	}
	
	private String getComputerName()
	{
	    Map<String, String> env = System.getenv();
	    if (env.containsKey("COMPUTERNAME"))
	        return env.get("COMPUTERNAME");
	    else if (env.containsKey("HOSTNAME"))
	        return env.get("HOSTNAME");
	    else
	        return "Unknown Computer";
	}
}
