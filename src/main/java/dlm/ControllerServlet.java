package dlm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import curl.ProcessExecutor;
import curl.ProcessManager;
import curl.ProcessManager.ManagedProcess;
import de.jwi.jspwiki.uptimeplugin.Uptime;
import dlm.util.VariableSubstitutor;

public class ControllerServlet extends HttpServlet
{
	private static final String VERSIONCONFIGFILE = "/version.txt";
	private static final String PROPERTIESFILE = "/WEB-INF/dlm.properties";
	private static final String CUSTOMPROPERTIESFILE = "/dlm-custom.properties";
	private static String version = "unknown";
	private static String builddate = "unknown";

	private ProcessManager processManager;
	private String hostAddress;
	private String hostname;
	
	Properties properties;

	private File downloadDir;
	private String canonicalDownloadPath;
	
	@Override
	public void init() throws ServletException
	{
		super.init();

		try
		{
			readVersion();
			
			hostname = readComputerName();
			if (hostname != null)
			{
				InetAddress address = InetAddress.getByName(hostname);
				hostAddress = address.getHostAddress();
			}
			
			properties = new Properties();
			
			URL url = getServletContext().getResource(PROPERTIESFILE);
			InputStream is = url.openStream();
			properties.load(is);
			is.close();
			
			url = getServletContext().getResource(CUSTOMPROPERTIESFILE);
			if (url != null)
			{
				Properties properties2 = new Properties();
				is = url.openStream();
				properties2.load(is);
				is.close();
				properties.putAll(properties2);
			}
			
			new VariableSubstitutor().substitute(properties);
			
			String d = properties.getProperty("downloaddir",".");
			downloadDir = new File(d);
			downloadDir.mkdirs();
			
			canonicalDownloadPath = downloadDir.getCanonicalPath();
			
		} catch (Exception e)
		{
			getServletContext().log(e.getMessage(), e);
		}

		processManager = new ProcessManager();
		processManager.init();
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

		if ("/dlsub".equals(servletPath))
		{
			doRemoteSubmit(request);
			return;
		}

		System.out.println(pathInfo);

		String action = request.getParameter("action");

		if ("Submit".equals(action))
		{
			String curl = request.getParameter("curl");
			System.out.println("submit: " + curl);
			processManager.runCommand(downloadDir, curl);
		}

		if ("remove".equals(action) || "kill".equals(action) || "restart".equals(action))
		{
			try
			{
				doMultipleActions(request, action);
			} catch (Exception e)
			{
				throw new ServletException(e);
			}
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

	private void doRemoteSubmit(HttpServletRequest request) throws ServletException
	{
		String curlCommand = request.getParameter("curl");
		try
		{
			curlCommand = URLDecoder.decode(curlCommand, "UTF-8");
			processManager.runCommand(downloadDir, curlCommand);
		} catch (UnsupportedEncodingException e)
		{
			throw new ServletException(e);
		}
	}

	private void doMultipleActions(HttpServletRequest request, String action) throws Exception
	{
		String[] selectedDownloads = request.getParameterValues("index");

		if (null == selectedDownloads)
		{
			return;
		}
		
		for (String id : selectedDownloads)
		{
			if ("kill".equals(action))
			{
				processManager.killProcessByID(id);
			}
			if ("remove".equals(action))
			{
				processManager.removeProcessByID(id);
			}
			if ("resubmit".equals(action))
			{
				processManager.resubmitProcessByID(id);
			}
		}
	}

	private void doTest(HttpServletRequest request) throws Exception
	{
		String[] args =
		{ "curl", "-V" };

		StringWriter sw = new StringWriter();

		ProcessExecutor processExecutor = new ProcessExecutor(args, downloadDir, sw);
		Integer status = processExecutor.call();

		request.setAttribute("curlV", sw.toString());
	}

	private void putServerInfo(HttpServletRequest request)
	{
		String contextPath = request.getContextPath();
		request.setAttribute("contextPath", contextPath);

		String serverInfo = getServletContext().getServerInfo();
		request.setAttribute("serverInfo", serverInfo);

		request.setAttribute("version", version);
		request.setAttribute("builddate", builddate);

		Runtime runtime = Runtime.getRuntime();
		long totalMemory = runtime.totalMemory(); // current heap allocated to
													// the VM process
		long freeMemory = runtime.freeMemory(); // out of the current heap, how
												// much is free
		long maxMemory = runtime.maxMemory(); // Max heap VM can use e.g. Xmx
												// setting

		double m = 1024 * 1024;

		DecimalFormat dec = new DecimalFormat("0.00");

		request.setAttribute("totalMemory", dec.format(totalMemory / m));
		request.setAttribute("freeMemory", dec.format(freeMemory / m));
		request.setAttribute("maxMemory", dec.format(maxMemory / m));

		long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(startTime);
		Date time = c.getTime();
		request.setAttribute("startTime", time);

		request.setAttribute("currentTime", new Date());
		
		request.setAttribute("downloaddir", canonicalDownloadPath);

		try
		{
			Uptime uptime = new Uptime();
			request.setAttribute("uptime", uptime.uptime());
			request.setAttribute("load", uptime.loadavg());

			request.setAttribute("hostname", hostname);
			request.setAttribute("hostAddress", hostAddress);

		} catch (Exception e)
		{
			getServletContext().log(e.getMessage(), e);
		}

		Collection<ManagedProcess> managedProcesses = processManager.getManagedProcesses();
		request.setAttribute("managedProcesses", managedProcesses);
	}

	private String readComputerName() throws IOException
	{
		Map<String, String> env = System.getenv();
		if (env.containsKey("COMPUTERNAME"))
		{
			// Windows
			return env.get("COMPUTERNAME");
		}

		String hostname = new Uptime().hostname();

		return hostname;
	}

	private void readVersion() throws IOException
	{
		String s;
	
		InputStream is = getClass().getResourceAsStream(VERSIONCONFIGFILE);

		if (is != null) {
			Properties versionProperties = new Properties();
			versionProperties.load(is);
			is.close();
			
			s = versionProperties.getProperty("version");
			if (null != s) {
				version = s;
			}
			
			s = versionProperties.getProperty("build.date");
			if (null != s) {
				builddate = s;
			}
		}
	}
}
