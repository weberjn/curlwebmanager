package edu.rice.cs.util;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ArgumentTokenizerTest
{
	@Test
	public void testArgumentTokenizer()
	{
		String c =

				"curl --header 'Host: releases.ubuntu.com'"
						+ " --header 'User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0'"
						+ " --header 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8' "
						+ " --header 'Accept-Language: en-US,en;q=0.5' --header 'Referer: http://releases.ubuntu.com/16.04/'"
						+ " --header 'Connection: keep-alive' --header 'Upgrade-Insecure-Requests: 1'"
						+ " 'http://releases.ubuntu.com/16.04/ubuntu-16.04.1-desktop-amd64.iso' -o 'ubuntu-16.04.1-desktop-amd64.iso' ";

		List<String> tokens = ArgumentTokenizer.tokenize(c);
		
		System.out.println(tokens);

		Assert.assertTrue(tokens.size()== 18);
	}
}
