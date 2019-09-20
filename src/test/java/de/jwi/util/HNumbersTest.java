package de.jwi.util;

import org.junit.Assert;
import org.junit.Test;

public class HNumbersTest {

	@Test
	public void testNums()
	{
		long l = HNumbers.parseLong("42");
		Assert.assertTrue(l == 42);

		l = HNumbers.parseLong(" 42K ");
		Assert.assertTrue(l == 42 * 1024);

		l = HNumbers.parseLong("42m ");
		Assert.assertTrue(l == 42 * 1024*1024);

		l = HNumbers.parseLong("8g ");
		long l1 = 8l * 1024 * 1024 * 1024;
		Assert.assertTrue(l == l1 );
	}

	@Test(expected = NumberFormatException.class)
	public void testIllegal()
	{
		long l = HNumbers.parseLong("x42");
	}
	
}
