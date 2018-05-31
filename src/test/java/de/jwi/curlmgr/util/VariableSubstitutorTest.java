package de.jwi.curlmgr.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import de.jwi.util.VariableSubstitutor;



public class VariableSubstitutorTest
{
	@Test
	public void testVars() throws IOException
	{
		Properties p = new Properties();
		InputStream is = VariableSubstitutorTest.class.getResourceAsStream("/testVars.properties");
		p.load(is);
		is.close();
		
		new VariableSubstitutor().substitute(p);
		
		p.list(System.out);
		
		String val = p.getProperty("listofThings");
		
		Assert.assertEquals("SomeValue, SomeValue.SomeOtherValue, constantValue", val);
	}
	
}
