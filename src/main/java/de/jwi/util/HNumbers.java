package de.jwi.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HNumbers {

	
	// from http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java/3758880
	public static String humanReadableByteCount(long bytes, boolean si) 
	{
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
	public static long parseLong(String s)
	{
		Pattern pattern = Pattern.compile("\\s*(\\d+)([kKmMgG])?(\\s*)");
		Matcher matcher = pattern.matcher(s);
		if (!matcher.matches())
		{
			throw new NumberFormatException(s);
		}
		long f = 1;
		
		long l = Integer.parseInt(matcher.group(1));
		
		String g = matcher.group(2);
		if (g != null)
		{
			if ("k".equalsIgnoreCase(g))
			{
				f = 1024l;
			}
			else if ("m".equalsIgnoreCase(g))
			{
				f = 1024l*1024l;
			}
			else if ("g".equalsIgnoreCase(g))
			{
				f = 1024l*1024l*1024l;
			}
		}
		
		l = l * f;
		
		return l;
	}
}
