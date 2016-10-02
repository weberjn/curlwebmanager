/* 
    Licensed to the Apache Software Foundation (ASF) under one
    or more contributor license agreements.  See the NOTICE file
    distributed with this work for additional information
    regarding copyright ownership.  The ASF licenses this file
    to you under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at
       http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.  
 */

package de.jwi.jspwiki.uptimeplugin;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Uptime
{
	public String uptime() throws IOException
	{
		String uptime = null;
		String s = null;

		String os = System.getProperty("os.name");
		if ("linux".equalsIgnoreCase(os))
		{
			FileInputStream fis = null;
			try
			{
				fis = new FileInputStream("/proc/uptime");
				s = new Scanner(fis).next();
			} finally
			{
				if (fis != null)
				{
					fis.close();
				}
			}

			if (s != null)
			{
				BigDecimal d = new BigDecimal(s);

				BigDecimal d1 = d.multiply(new BigDecimal("1000"));

				long l = d1.longValue();

				long days = TimeUnit.MILLISECONDS.toDays(l);
				long hours = TimeUnit.MILLISECONDS.toHours(l - TimeUnit.MILLISECONDS.convert(days, TimeUnit.DAYS));
				long minutes = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.MILLISECONDS.convert(days, TimeUnit.DAYS)
						- TimeUnit.MILLISECONDS.convert(hours, TimeUnit.HOURS));

				uptime = String.format("%d days, %d:%02d%n", days, hours, minutes);
			}
		}

		return uptime;
	}

	public String loadavg() throws IOException
	{
		String loadavg = null;
		String s = null;

		String os = System.getProperty("os.name");
		if ("linux".equalsIgnoreCase(os))
		{
			FileInputStream fis = null;
			try
			{
				fis = new FileInputStream("/proc/loadavg");
				s = new Scanner(fis).nextLine();
			} finally
			{
				if (fis != null)
				{
					fis.close();
				}
			}

			if (s != null)
			{
				String[] sl = s.split("\\s+");

				loadavg = String.format("%s, %s, %s", sl[0], sl[1], sl[2]);
			}
		}

		return loadavg;
	}

}
