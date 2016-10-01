package dlm.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandLineSplitter
{

	private Pattern pattern;

	public CommandLineSplitter()
	{
		// http://stackoverflow.com/a/22472588

		pattern = Pattern.compile("[^\\s]*\"(\\\\+\"|[^\"])*?\"|[^\\s]*'(\\\\+'|[^'])*?'|(\\\\\\s|[^\\s])+",
				Pattern.MULTILINE);
	}

	public String[] splitCommandLine(String commandLine)
	{
		Matcher matcher = pattern.matcher(commandLine);

		List<String> matches = new ArrayList<String>();
		while (matcher.find())
		{
			String s = matcher.group();
			if (s.charAt(0) == '\'' && s.charAt(s.length() - 1) == '\'')
			{
				s = s.substring(1, s.length()-1);
			}
			matches.add(s);
		}
		String[] parsedCommand = matches.toArray(new String[]
		{});
		
		return parsedCommand;
	}

	public static void main(String[] args) throws Exception
	{
		String c =

				"curl --header 'Host: releases.ubuntu.com'"
						+ " --header 'User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0'"
						+ " --header 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8' "
						+ "--header 'Accept-Language: en-US,en;q=0.5' --header 'Referer: http://releases.ubuntu.com/16.04/'"
						+ "--header 'Connection: keep-alive' --header 'Upgrade-Insecure-Requests: 1' "
						+ "http://releases.ubuntu.com/16.04/ubuntu-16.04.1-desktop-amd64.iso' -o 'ubuntu-16.04.1-desktop-amd64.iso' ";

		String[] splitCommandLine = new CommandLineSplitter().splitCommandLine(c);
		
		System.out.println(Arrays.asList(splitCommandLine));
		
	}

}
